package com.github.skynet3285.exposed.exception.translator.example.registration

import org.jetbrains.exposed.v1.jdbc.insert
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SampleUserRepository {
    @Transactional
    fun saveUser(name: String) {
        UserTable.insert {
            it[UserTable.name] = name
        }
    }
}
