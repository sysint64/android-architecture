package ru.kabylin.androidarchexample.forms.validators

import android.content.res.Resources
import ru.kabylin.androidarchexample.R

class MaxMinStringValidator(resources: Resources, val min: Int, val max: Int): Validator(resources) {
    override fun isValid(value: Any?): Boolean {
        if (value !is String)
            throw UnsupportedOperationException()

        return value.length in min..max
    }

    override fun getErrorText(value: Any?): String {
        if (value !is CharSequence)
            throw UnsupportedOperationException()

        return when {
            value.length < min -> {
                val format = resources!!.getString(R.string.min_error)
                String.format(format, min)
            }
            value.length > max -> {
                val format = resources!!.getString(R.string.max_error)
                String.format(format, max)
            }
            else -> throw UnsupportedOperationException()
        }
    }
}
