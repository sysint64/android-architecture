package ru.kabylin.androidarchexample.client.api

import ru.kabylin.androidarchexample.client.ClientError
import ru.kabylin.androidarchexample.client.api.impl.MapApiValidationError
import ru.kabylin.androidarchexample.client.api.impl.SingleApiValidationError

class ApiErrorException(val error: ClientError) : RuntimeException()

fun apiValidationException(field: String, message: String): ApiErrorException {
    return ApiErrorException(SingleApiValidationError(field, message))
}

fun apiValidationException(errors: Map<String, String>): ApiErrorException {
    return ApiErrorException(MapApiValidationError(errors))
}
