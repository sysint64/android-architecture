package ru.kabylin.androidarchexample.client.api.impl

import ru.kabylin.androidarchexample.client.ClientError

class ApiErrorImpl(
    override val message: String
) : ClientError
