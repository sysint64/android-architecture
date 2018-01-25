package ru.kabylin.androidarchexample.common

interface Service {
    companion object {
        val REQUEST_DEFAULT = 2000
    }

    var requestCode: Int

    fun withRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    fun currentRequestCode(): Int = this.requestCode
}
