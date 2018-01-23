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
class AuthServiceTest : BaseTest() {
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        // TODO: Нужен сервис для чистки базы данных
    }

    @Test
    fun login() {
        RegistrationServiceTest().finishRegistration()

        val service: AuthService = kodein.instance()

        service.login(Constants.phone, Constants.password)
            .map { it }
            .test {
                it shouldHave noErrors()
            }
    }

    @Test
    fun logout() {
        RegistrationServiceTest().finishRegistration()

        val service: AuthService = kodein.instance()

        service.login(Constants.phone, Constants.password)
            .flatMap {
                service.logout(Constants.phone)
            }
            .test {
                it shouldHave noErrors()
            }
    }
}
