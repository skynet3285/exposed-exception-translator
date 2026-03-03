package com.github.skynet3285.exposed.exception.translator.support

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.getBeanProvider
import org.springframework.dao.support.DataAccessUtils
import org.springframework.dao.support.PersistenceExceptionTranslator
import org.springframework.jdbc.support.SQLExceptionTranslator
import org.springframework.util.ReflectionUtils
import java.sql.SQLException

// Reference: org.springframework.dao.support.PersistenceExceptionTranslationInterceptor
// https://github.com/spring-projects/spring-framework/blob/v7.0.2/spring-tx/src/main/java/org/springframework/dao/support/PersistenceExceptionTranslationInterceptor.java
class ExposedExceptionTranslationInterceptor(
    private var beanFactory: BeanFactory,
    var alwaysTranslate: Boolean = false,
) : MethodInterceptor,
    BeanFactoryAware,
    InitializingBean {
    @Volatile
    private var persistenceExceptionTranslator: PersistenceExceptionTranslator? = null

    @Volatile
    private var exposedTranslator: SQLExceptionTranslator? = null

    init {
        ensureListableBeanFactory(beanFactory)
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        ensureListableBeanFactory(beanFactory)
        this.beanFactory = beanFactory
    }

    private fun ensureListableBeanFactory(bf: BeanFactory) {
        if (bf !is ListableBeanFactory) {
            throw IllegalArgumentException("Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory")
        }
    }

    override fun afterPropertiesSet() {}

    override fun invoke(mi: MethodInvocation): Any? {
        try {
            return mi.proceed()
        } catch (ex: Exception) {
            if (ex is SQLException || ex is RuntimeException) {
                // Let it throw raw if the type of the exception is on the throws clause of the method.
                if (!alwaysTranslate && ReflectionUtils.declaresException(mi.method, ex.javaClass)) {
                    throw ex
                }

                // RuntimeException (PersistenceException, JPA Exception, ETC...)
                if (ex is RuntimeException) {
                    val translator = getPersistenceExceptionTranslator()

                    throw DataAccessUtils.translateIfNecessary(ex, translator)
                }

                // SQLException (ExposedSQLException, JDBC's Exception, ETC...)
                if (ex is SQLException) {
                    val translator = getSQLExceptionTranslator()

                    throw translator.translate("Exposed Operation", null, ex) ?: ex
                }
            }

            throw ex
        }
    }

    private fun getSQLExceptionTranslator(): SQLExceptionTranslator =
        exposedTranslator ?: synchronized(this) {
            exposedTranslator ?: detectSQLExceptionTranslator(beanFactory as ListableBeanFactory).also {
                exposedTranslator = it
            }
        }

    private fun detectSQLExceptionTranslator(bf: ListableBeanFactory): SQLExceptionTranslator =
        bf.getBeanProvider<SQLExceptionTranslator>().ifAvailable
            ?: throw IllegalStateException("No SQLExceptionTranslator bean found in context")

    private fun getPersistenceExceptionTranslator(): PersistenceExceptionTranslator =
        persistenceExceptionTranslator ?: synchronized(this) {
            persistenceExceptionTranslator ?: detectPersistenceExceptionTranslators(beanFactory as ListableBeanFactory).also {
                persistenceExceptionTranslator = it
            }
        }

    private fun detectPersistenceExceptionTranslators(bf: ListableBeanFactory): PersistenceExceptionTranslator {
        val translators =
            bf
                .getBeanProvider(PersistenceExceptionTranslator::class.java, false)
                .orderedStream()
                .toList()

        return PersistenceExceptionTranslator { ex ->
            for (translator in translators) {
                val translated = translator.translateExceptionIfPossible(ex)
                if (translated != null) return@PersistenceExceptionTranslator translated
            }
            null
        }
    }
}
