package ru.kabylin.androidarchexample.client

interface RequestStateListener {
    fun onStartRequest(requestCode: Int)
    fun onFinishRequest(requestCode: Int)
}
