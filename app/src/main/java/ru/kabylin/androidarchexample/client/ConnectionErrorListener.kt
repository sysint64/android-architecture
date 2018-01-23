package ru.kabylin.androidarchexample.client

interface ConnectionErrorListener {
    fun timeoutError(requestCode: Int)

    fun connectionError(requestCode: Int)
}
