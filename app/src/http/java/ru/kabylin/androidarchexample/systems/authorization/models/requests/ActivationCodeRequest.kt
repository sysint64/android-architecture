package ru.kabylin.androidarchexample.systems.authorization.models.requests

data class ActivationCodeRequest(
    val phone: String,
    val type_request: Int
)
