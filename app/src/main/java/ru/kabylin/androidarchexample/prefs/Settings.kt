package ru.kabylin.androidarchexample.prefs

import com.chibatching.kotpref.KotprefModel

object Settings : KotprefModel() {
    var pushNotifications by booleanPref(default = true)
    var passwordWasSet by booleanPref(default = false)
}
