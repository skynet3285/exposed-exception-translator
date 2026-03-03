package com.github.skynet3285.exposed.exception.translator.example.registration

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.spring.boot4.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataAccessException
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(
    classes = [
        MemoryBasedDatabaseConfig::class,
        ExposedAutoConfiguration::class,
        ExposedExceptionTranslatorConfig::class,
        SampleUserRepository::class,
    ],
)
@Transactional
class ManualRegistrationTest(
    private val userRepository: SampleUserRepository,
) : BehaviorSpec({
        isolationMode = IsolationMode.InstancePerRoot

        Given("ExposedExceptionTranslatorConfig은 Repository에 Exposed 예외 변환기가 적용된다") {
            When("Repository에서 중복된 이름을 저장하려고 하면") {
                SchemaUtils.create(UserTable)
                val name = "Jane Doe"
                userRepository.saveUser(name)

                Then("Repository에서 중복된 이름을 저장하면 Spring의 DataAccessException이 던져져야 한다") {
                    shouldThrow<DataAccessException> {
                        userRepository.saveUser(name)
                    }
                }
            }
        }
    })
