package ru.kabylin.androidarchexample.client

import ru.kabylin.androidarchexample.services.Service

interface AwaitableRequest {
    fun awaitRequest(requestCode: Int = Service.REQUEST_DEFAULT)
}
