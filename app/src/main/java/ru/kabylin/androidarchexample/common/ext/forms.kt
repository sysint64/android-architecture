package ru.kabylin.androidarchexample.common.ext

import android.util.Log
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.forms.Form

fun Form.setApiValidationErrors(errors: ApiValidationError) =
    setErrors(errors.errors)

fun Form.setErrors(errors: Map<String, String>) {
    for (error in errors)
        setErrorForField(error.key, error.value)
}
