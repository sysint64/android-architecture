package ru.kabylin.androidarchexample.views

import ru.kabylin.androidarchexample.Screen

interface ViewStateHolder {
    val screen: Screen

    fun viewStateEdenUpdate() {}

    fun viewStateFullUpdate() {}

    fun viewStateInBackground(): Boolean
}
