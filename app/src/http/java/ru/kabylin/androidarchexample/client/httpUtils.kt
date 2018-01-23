package ru.kabylin.androidarchexample.client

fun <T> createRetrofitService(client: Client, cls: Class<T>): T =
    when (client) {
        is HttpClient -> client.retrofit.create(cls)
        else -> throw UnsupportedOperationException()
    }
