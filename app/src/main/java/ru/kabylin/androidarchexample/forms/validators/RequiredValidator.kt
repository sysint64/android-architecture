package ru.kabylin.androidarchexample.forms.validators

import android.content.res.Resources
import ru.kabylin.androidarchexample.R

class RequiredValidator(resources: Resources? = null) : Validator(resources) {
    override fun getErrorText(value: Any?): String =
        errorFromRes(R.string.required_error)

    override fun isValid(value: Any?): Boolean =
        when (value) {
            value == null -> false
            is String -> !value.isBlank()
            is Boolean -> value
            else -> throw UnsupportedOperationException()
        }
}
