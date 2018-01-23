package ru.kabylin.androidarchexample.systems.authorization.services

import io.reactivex.Single
import ru.kabylin.androidarchexample.credentials.Credentials
import ru.kabylin.androidarchexample.services.Service

interface AuthService : Service {
    fun login(phone: String, password: String): Single<out Credentials>

    fun updateToken(phone: String, refreshToken: String): Single<out Credentials>

    fun logout(phone: String): Single<out Unit>
}
