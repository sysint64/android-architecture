package ru.kabylin.androidarchexample.client

interface AccessErrorListener {
    fun notAuthenticatedError(requestCode: Int, error: ClientError)

    fun permissionDeniedError(requestCode: Int, error: ClientError)

    fun throttledError(requestCode: Int, error: ClientError)
}
