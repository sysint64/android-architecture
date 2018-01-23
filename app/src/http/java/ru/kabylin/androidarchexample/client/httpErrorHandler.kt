package ru.kabylin.androidarchexample.client

import com.github.salomonbrys.kodein.KodeinInjector
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import ru.kabylin.androidarchexample.client.api.*
import ru.kabylin.androidarchexample.client.api.impl.*

/**
 * Методы извлекает ApiError из json ответа.
 */
fun retrieveApiErrorFromHttpException(exception: HttpException): ApiError? {
    try {
        val json = exception.response().errorBody()?.string() ?: return null
        val errorResponse = Gson().fromJson(json, ErrorResponse::class.java)

        return when (errorResponse.code) {
            ApiErrorCode.VALIDATION_ERROR.code ->
                ApiResponseValidationError(errorResponse)

            else -> ApiResponseError(errorResponse)
        }
    } catch (e: JsonSyntaxException) {
        return null
    }
}

fun handleHttpError(requestCode: Int, exception: HttpException, injector: KodeinInjector) {
    val error = retrieveApiErrorFromHttpException(exception)

    if (error != null) {
        handleApiError(requestCode, error, injector)
    }
}
