package ru.kabylin.androidarchexample.client.api

import ru.kabylin.androidarchexample.client.ClientError
import ru.kabylin.androidarchexample.client.api.impl.ApiErrorCode

interface ApiError : ClientError {
    val code: ApiErrorCode
}
