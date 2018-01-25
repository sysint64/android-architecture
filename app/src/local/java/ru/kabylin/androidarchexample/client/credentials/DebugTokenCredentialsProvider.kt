package ru.kabylin.androidarchexample.client.credentials

import ru.kabylin.androidarchexample.prefs.StoredAccessTokenCredentials
import ru.kabylin.androidarchexample.prefs.StoredUserInfo

class DebugTokenCredentialsProvider : CredentialsProvider {
    private var credentials: DebugTokenCredentials? = null

    override fun provided(): Boolean = credentials != null

    override fun provide(): Credentials? {
        if (StoredUserInfo.phone.trim() == "") {
            // Нет никаких данных о пользователе => нечего возвращать
            credentials = null
            return null
        }

        credentials = with(StoredAccessTokenCredentials) {
            DebugTokenCredentials(
                phone = StoredUserInfo.phone,
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiredTime = expired
            )
        }

        return credentials
    }
}
