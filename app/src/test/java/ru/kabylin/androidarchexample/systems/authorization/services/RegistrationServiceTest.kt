package ru.kabylin.androidarchexample.systems.authorization.services

import com.github.salomonbrys.kodein.instance
import com.rubylichtenstein.rxtest.assertions.shouldHave
import com.rubylichtenstein.rxtest.extentions.test
import com.rubylichtenstein.rxtest.matchers.noErrors
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.kabylin.androidarchexample.common.BaseTest
import ru.kabylin.androidarchexample.common.Constants

@RunWith(RobolectricTestRunner::class)
class RegistrationServiceTest : BaseTest() {
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        // TODO: Нужен сервис для чистки базы данных
    }

    @Test
    fun requestRegistration() {
        val service: RegistrationService = kodein.instance()

        service
            .requestRegistration(Constants.phone)
            .map { it }
            .test {
                it shouldHave noErrors()
            }
    }

    @Test
    fun finishRegistration() {
        val service: RegistrationService = kodein.instance()

        service
            .requestRegistration(Constants.phone)
            .flatMap {
                service.finishRegistration(
                    password = Constants.password,
                    secretCode =  it,
                    activationCode = "1111")
            }
            .test {
                it shouldHave noErrors()
            }
    }
}
