package ru.kabylin.androidarchexample.client.api.impl

import android.util.Log
import ru.kabylin.androidarchexample.client.api.ApiValidationError

class ApiResponseValidationError(errorResponse: ErrorResponse) : ApiValidationError, ApiResponseError(errorResponse) {
    override val errors: Map<String, String> = HashMap()

    init {
        val errors = this.errors as HashMap

        Log.d("HttpClient", errorResponse.error.toString())
        for ((key, value) in errorResponse.error) {
            if (value !is List<*>) {
                Log.d("HttpClient", value.toString())
                continue
            }

            val message = value.firstOrNull()

            if (message == null || message !is String) {
                continue
            }

            errors[key] = message
        }

        Log.e("HttpClient", this.errors.toString())
    }
}
