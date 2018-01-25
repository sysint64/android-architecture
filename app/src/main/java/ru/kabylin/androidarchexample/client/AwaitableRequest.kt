package ru.kabylin.androidarchexample.client

import ru.kabylin.androidarchexample.common.Service

interface AwaitableRequest {
    fun awaitRequest(requestCode: Int = Service.REQUEST_DEFAULT)
}
