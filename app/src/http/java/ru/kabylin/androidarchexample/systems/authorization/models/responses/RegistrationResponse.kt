package ru.kabylin.androidarchexample.systems.authorization.models.responses

data class RegistrationResponse(
    val access_token: String,
    val refresh_token: String
)