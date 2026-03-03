package com.github.skynet3285.exposed.exception.translator.support

import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import java.sql.SQLException
import javax.sql.DataSource

class ExposedExceptionTranslator(
    dataSource: DataSource,
) : SQLErrorCodeSQLExceptionTranslator(dataSource) {
    override fun translate(
        task: String,
        sql: String?,
        ex: SQLException,
    ): DataAccessException? {
        val actualSqlEx =
            if (ex is ExposedSQLException) {
                ex.cause as? SQLException ?: ex
            } else {
                ex
            }

        return super.translate(task, sql, actualSqlEx)
    }
}
