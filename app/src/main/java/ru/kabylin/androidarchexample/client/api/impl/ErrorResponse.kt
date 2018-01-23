package ru.kabylin.androidarchexample.client.api.impl

data class ErrorResponse(
    val code: Int,
    val error: Map<String, Any>
)
