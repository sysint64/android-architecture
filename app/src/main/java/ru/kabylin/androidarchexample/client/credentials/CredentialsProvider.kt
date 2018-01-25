package ru.kabylin.androidarchexample.client.credentials

interface CredentialsProvider {
    fun provided(): Boolean

    fun provide(): Credentials?
}
