package com.github.skynet3285.exposed.exception.translator.example.registration

import org.jetbrains.exposed.v1.core.Table

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50).uniqueIndex()
    override val primaryKey = PrimaryKey(id)
}
