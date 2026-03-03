package com.github.skynet3285.exposed.exception.translator.example.registration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class MemoryBasedDatabaseConfig {
    @Bean
    fun dataSource(): DataSource {
        val config =
            HikariConfig().apply {
                jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
                username = "sa"
                password = ""
                driverClassName = "org.h2.Driver"
                maximumPoolSize = 10
            }
        return HikariDataSource(config)
    }
}
