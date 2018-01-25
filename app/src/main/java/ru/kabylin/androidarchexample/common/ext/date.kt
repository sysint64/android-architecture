package ru.kabylin.androidarchexample.common.ext

import java.util.*

val Date.calendar: Calendar
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar
    }

fun Date.add(value: Int = 0, units: Int = Calendar.DAY_OF_MONTH): Date {
    val calendar = this.calendar
    calendar.add(units, value)
    return calendar.time
}

val Date.age: Int
    get() = this.diffTo(now())

fun now(): Date = Calendar.getInstance().time

/**
 * Сравниваем объект даты (слева) с [date].
 * [date] должен быть окончанием диапазона дат.
 */
fun Date.diffTo(date: Date): Int {
    val a = this.calendar
    val b = date.calendar

    var diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)

    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE)) {
        diff--
    }

    return diff
}

fun getDate(year: Int, month: Int, day: Int): Date =
    with(Calendar.getInstance()) {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1) // the month between 0-11
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        return time
    }
