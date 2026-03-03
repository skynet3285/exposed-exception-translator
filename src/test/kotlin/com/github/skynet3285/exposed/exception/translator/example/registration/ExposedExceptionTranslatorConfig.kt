package com.github.skynet3285.exposed.exception.translator.example.registration

import com.github.skynet3285.exposed.exception.translator.annotation.ExposedExceptionTranslationPostProcessor
import com.github.skynet3285.exposed.exception.translator.support.ExposedExceptionTranslator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class ExposedExceptionTranslatorConfig {
    @Bean
    fun exposedExceptionTranslator(dataSource: DataSource) = ExposedExceptionTranslator(dataSource)

    @Bean
    fun exposedExceptionTranslationPostProcessor(): ExposedExceptionTranslationPostProcessor = ExposedExceptionTranslationPostProcessor()
}
