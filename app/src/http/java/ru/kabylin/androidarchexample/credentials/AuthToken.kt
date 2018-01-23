package ru.kabylin.androidarchexample.credentials

import ru.kabylin.androidarchexample.prefs.StoredAccessTokenCredentials

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
): Credentials {
    override fun revoke() {
        with(StoredAccessTokenCredentials) {
            accessToken = ""
            refreshToken = ""
        }
    }

    override fun hasExpired(): Boolean = false

    override fun getPublicKey(): ByteArray = "".toByteArray()

    override fun getPrivateKey(): ByteArray = refreshToken.toByteArray()

    override fun store() {
        with(StoredAccessTokenCredentials) {
            accessToken = this@AuthToken.accessToken
            refreshToken = this@AuthToken.refreshToken
        }
    }
}