package ru.kabylin.androidarchexample.client.api

interface ApiValidationErrorListener {
    fun onApiValidationError(requestCode: Int, errors: ApiValidationError)
}
