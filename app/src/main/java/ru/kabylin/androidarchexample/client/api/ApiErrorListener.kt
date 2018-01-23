package ru.kabylin.androidarchexample.client.api

interface ApiErrorListener {
    fun onApiError(requestCode: Int, error: ApiError)
}
