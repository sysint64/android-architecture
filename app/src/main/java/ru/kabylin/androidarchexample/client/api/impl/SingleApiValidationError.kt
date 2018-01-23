package ru.kabylin.androidarchexample.client.api.impl

import ru.kabylin.androidarchexample.client.api.ApiValidationError

class SingleApiValidationError(
    field: String,
    messageForField: String,
    override val message: String = ""
) : ApiValidationError {
    override val errors: Map<String, String> = mapOf(field to messageForField)
    override val code: ApiErrorCode = ApiErrorCode.VALIDATION_ERROR
}
