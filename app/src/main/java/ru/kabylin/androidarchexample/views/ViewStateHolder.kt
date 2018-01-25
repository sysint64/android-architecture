package ru.kabylin.androidarchexample.views

interface ViewStateHolder {
    fun viewStateEdenUpdate() {}

    fun viewStateFullUpdate() {}

    fun viewStateInBackground(): Boolean
}
