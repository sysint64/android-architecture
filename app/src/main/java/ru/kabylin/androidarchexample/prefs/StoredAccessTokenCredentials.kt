package ru.kabylin.androidarchexample.prefs

import com.chibatching.kotpref.KotprefModel

object StoredAccessTokenCredentials : KotprefModel() {
    var accessToken by stringPref()
    var refreshToken by stringPref()
    var expired by longPref(default = 0)
}
