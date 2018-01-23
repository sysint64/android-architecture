package ru.kabylin.androidarchexample.client.api.impl

import ru.kabylin.androidarchexample.client.api.ApiValidationError

class MapApiValidationError(
    override val errors: Map<String, String>,
    override val message: String = ""
) : ApiValidationError {
    override val code: ApiErrorCode = ApiErrorCode.VALIDATION_ERROR
}
