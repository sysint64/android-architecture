package ru.kabylin.androidarchexample.client

data class HttpResponse(
    val statusCode: Int,
    val body: ByteArray?,
    val message: String,
    val headers: Map<String, String>
)
