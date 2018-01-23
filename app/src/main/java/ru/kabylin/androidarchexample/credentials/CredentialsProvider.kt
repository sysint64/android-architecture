package ru.kabylin.androidarchexample.credentials

interface CredentialsProvider {
    fun provided(): Boolean

    fun provide(): Credentials?
}
