package ru.kabylin.androidarchexample.utility.ext

import android.graphics.Point

fun Point.toFloatArray(): FloatArray {
    return floatArrayOf(
        x.toFloat(),
        y.toFloat())
}