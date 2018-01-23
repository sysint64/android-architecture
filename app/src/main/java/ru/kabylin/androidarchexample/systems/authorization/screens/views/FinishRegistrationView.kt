package ru.kabylin.androidarchexample.systems.authorization.screens.views

import ru.kabylin.androidarchexample.credentials.Credentials

interface FinishRegistrationView {
    fun onRegistrationFinished(credentials: Credentials)
}
