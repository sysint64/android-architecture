package ru.kabylin.androidarchexample.client.api.impl

import ru.kabylin.androidarchexample.client.api.ApiError

open class ApiResponseError(errorResponse: ErrorResponse) : ApiError {
    override val code: ApiErrorCode = ApiErrorCode.fromInt(errorResponse.code)
    override val message = if ("detail" in errorResponse.error) {
        errorResponse.error["detail"] as String
    } else ""
}
