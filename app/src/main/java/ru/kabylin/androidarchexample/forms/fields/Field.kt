package ru.kabylin.androidarchexample.forms.fields

import android.view.ViewGroup
import ru.kabylin.androidarchexample.forms.Form
import ru.kabylin.androidarchexample.forms.validators.Validator

abstract class Field(val name: String, var exclude: Boolean = false) {
    lateinit var form: Form
    var validators: List<Validator> = listOf()
        set (value) {
            field = value

            for (validator in value)
                validator.resources = form.context.resources
        }

    abstract var isEnabled: Boolean

    abstract fun getValue(): Any

    abstract fun inflate(parent: ViewGroup)

    abstract fun setError(error: String)

    abstract fun clearError()

    abstract fun setVisible(isVisible: Boolean)

    fun isValid(): Boolean {
        val value = getValue()

        for (validator in validators) {
            clearError()

            val (isHookType, isValid) = validator.isHooksValid(value)
            val validatorIsValid = if (isHookType) isValid else validator.isValid(value)

            if (validatorIsValid)
                continue

            /// Set error text
            val (isHookErrorTextType, errorStringRes) = validator.isErrorTextHook(value)

            if (isHookErrorTextType) {
                setError(errorStringRes)
            } else {
                setError(validator.getErrorText(value))
            }

            return false
        }

        return true
    }
}
