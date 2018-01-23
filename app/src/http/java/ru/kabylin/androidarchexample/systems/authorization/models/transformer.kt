package ru.kabylin.androidarchexample.systems.authorization.models

import ru.kabylin.androidarchexample.credentials.AuthToken
import ru.kabylin.androidarchexample.credentials.Credentials
import ru.kabylin.androidarchexample.systems.authorization.models.responses.LoginResponse

val MALE = "M"
val FEMALE = "F"

enum class CodeRequest(val code: Int) {
    REGISTRATION(0),
    CHANGE_PASSWORD(1);
}

fun LoginResponse.toCredentials(): Credentials {
    return AuthToken(
        accessToken = access_token,
        refreshToken = refresh_token
    )
}
