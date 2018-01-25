package ru.kabylin.androidarchexample.common.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.toText(format: D2DateFormats): String {
    return format.formatter.format(this)
}

fun String.toDate(format: D2DateFormats): Date {
    return format.formatter.parse(this)
}

enum class D2DateFormats(val pattern: String) {
    SIMPLE("dd.MM.yyyy"),
    SERVER("yyyy-MM-dd")
}

val D2DateFormats.formatter: SimpleDateFormat
    get() = SimpleDateFormat(this.pattern, Locale.US)
