package ru.kabylin.androidarchexample.systems.authorization.models.requests

data class RegistrationRequest(
    val password: String,
    val secret_code: String,
    val activation_code: String
)
