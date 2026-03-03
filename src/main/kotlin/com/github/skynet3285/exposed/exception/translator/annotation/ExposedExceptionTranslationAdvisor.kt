package com.github.skynet3285.exposed.exception.translator.annotation

import com.github.skynet3285.exposed.exception.translator.support.ExposedExceptionTranslationInterceptor
import org.aopalliance.aop.Advice
import org.springframework.aop.Pointcut
import org.springframework.aop.support.AbstractPointcutAdvisor
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut
import org.springframework.beans.factory.ListableBeanFactory

// Reference: org.springframework.dao.annotation.PersistenceExceptionTranslationAdvisor
// https://github.com/spring-projects/spring-framework/blob/v7.0.2/spring-tx/src/main/java/org/springframework/dao/annotation/PersistenceExceptionTranslationAdvisor.java
class ExposedExceptionTranslationAdvisor(
    beanFactory: ListableBeanFactory,
    repositoryAnnotationType: Class<out Annotation>,
) : AbstractPointcutAdvisor() {
    private val advice: ExposedExceptionTranslationInterceptor = ExposedExceptionTranslationInterceptor(beanFactory)
    private val pointcut: AnnotationMatchingPointcut = AnnotationMatchingPointcut(repositoryAnnotationType, true)

    override fun getAdvice(): Advice = this.advice

    override fun getPointcut(): Pointcut = this.pointcut
}
