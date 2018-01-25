package ru.kabylin.androidarchexample.client.credentials

import ru.kabylin.androidarchexample.prefs.StoredAccessTokenCredentials
import ru.kabylin.androidarchexample.prefs.StoredUserInfo
import java.util.*
import ru.kabylin.androidarchexample.common.ext.*

class DebugTokenCredentials(
    val phone: String,
    val accessToken: String,
    val refreshToken: String,
    val expiredTime: Long = now().add(10, Calendar.MINUTE).time
) : Credentials {
    override fun revoke() {
        with(StoredAccessTokenCredentials) {
            accessToken = ""
            refreshToken = ""
            expired = 0
        }
    }

    override fun hasExpired(): Boolean = expiredTime <= now().time

    override fun getPublicKey(): ByteArray = phone.toByteArray()

    override fun getPrivateKey(): ByteArray = accessToken.toByteArray()

    override fun store() {
        with(StoredAccessTokenCredentials) {
            accessToken = this@DebugTokenCredentials.accessToken
            refreshToken = this@DebugTokenCredentials.refreshToken
            expired = expiredTime
        }

        StoredUserInfo.phone = phone
    }
}
