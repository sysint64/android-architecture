package ru.kabylin.androidarchexample.client.api

import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instanceOrNull
import ru.kabylin.androidarchexample.client.AccessErrorListener
import ru.kabylin.androidarchexample.client.ClientError
import ru.kabylin.androidarchexample.client.api.impl.ApiErrorCode

fun handleApiError(requestCode: Int, error: ClientError, injector: KodeinInjector) {
    val apiValidationErrorListener: ApiValidationErrorListener? by injector.instanceOrNull()
    val accessErrorListener: AccessErrorListener? by injector.instanceOrNull()
    val apiErrorListener: ApiErrorListener? by injector.instanceOrNull()

    if (error !is ApiError)
        return

    when (error.code) {
        ApiErrorCode.VALIDATION_ERROR ->
            apiValidationErrorListener?.onApiValidationError(requestCode, error as ApiValidationError)

        ApiErrorCode.NOT_AUTHENTICATED ->
            accessErrorListener?.notAuthenticatedError(requestCode, error)

        ApiErrorCode.PERMISSION_DENIED ->
            accessErrorListener?.permissionDeniedError(requestCode, error)

        ApiErrorCode.THROTTLED ->
            accessErrorListener?.throttledError(requestCode, error)

        else ->
            apiErrorListener?.onApiError(requestCode, error)
    }
}

fun handleApiError(requestCode: Int, exception: ApiErrorException, injector: KodeinInjector) =
    handleApiError(requestCode, exception.error, injector)
