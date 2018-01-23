package ru.kabylin.androidarchexample.prefs

import com.chibatching.kotpref.KotprefModel

object StoredUserInfo : KotprefModel() {
    var userId by stringPref(default = "")
    var phone by stringPref(default = "")
    var password by stringPref(default = "")
}
