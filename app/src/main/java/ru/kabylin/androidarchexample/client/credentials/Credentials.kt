package ru.kabylin.androidarchexample.client.credentials

interface Credentials {
    fun getPublicKey(): ByteArray
    fun getPrivateKey(): ByteArray
    fun hasExpired(): Boolean
    fun store()
    fun revoke()
}
