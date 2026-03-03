package com.github.skynet3285.exposed.exception.translator.annotation

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.stereotype.Repository
import org.springframework.util.Assert

// Reference: org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
// https://github.com/spring-projects/spring-framework/blob/v7.0.2/spring-tx/src/main/java/org/springframework/dao/annotation/PersistenceExceptionTranslationPostProcessor.java
@Suppress("serial")
class ExposedExceptionTranslationPostProcessor : AbstractBeanFactoryAwareAdvisingPostProcessor() {
    private var repositoryAnnotationType: Class<out Annotation> = Repository::class.java

    @Suppress("unused")
    fun setRepositoryAnnotationType(repositoryAnnotationType: Class<out Annotation>) {
        Assert.notNull(repositoryAnnotationType, "'repositoryAnnotationType' must not be null")
        this.repositoryAnnotationType = repositoryAnnotationType
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        super.setBeanFactory(beanFactory)

        if (beanFactory !is ListableBeanFactory) {
            throw IllegalArgumentException(
                "Cannot use ExposedExceptionTranslator autodetection without ListableBeanFactory",
            )
        }

        this.advisor = ExposedExceptionTranslationAdvisor(beanFactory, this.repositoryAnnotationType)
    }
}
