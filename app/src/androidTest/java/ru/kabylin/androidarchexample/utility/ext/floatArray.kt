package ru.kabylin.androidarchexample.utility.ext

import android.graphics.Point

fun FloatArray.toPoint(): Point? {
    if (this.count() != 2)
        return null

    return Point(
        this[0].toInt(),
        this[1].toInt())
}
