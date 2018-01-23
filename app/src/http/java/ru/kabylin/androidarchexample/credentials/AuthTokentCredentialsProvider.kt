package ru.kabylin.androidarchexample.credentials

import ru.kabylin.androidarchexample.prefs.StoredAccessTokenCredentials
import ru.kabylin.androidarchexample.prefs.StoredUserInfo

class AuthTokentCredentialsProvider : CredentialsProvider {
    private var credentials: AuthToken? = null

    override fun provided(): Boolean = credentials != null

    override fun provide(): Credentials? {
        if (StoredUserInfo.phone.trim() == "") {
            //Нет никаких данных о пользователе => нечего возвращать
            credentials = null
            return null
        }

        credentials = with(StoredAccessTokenCredentials) {
            AuthToken(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }

        return credentials
    }
}
