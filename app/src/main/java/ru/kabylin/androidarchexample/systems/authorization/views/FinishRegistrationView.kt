package ru.kabylin.androidarchexample.systems.authorization.views

import ru.kabylin.androidarchexample.client.credentials.Credentials

interface FinishRegistrationView {
    fun onRegistrationFinished(credentials: Credentials)
}
