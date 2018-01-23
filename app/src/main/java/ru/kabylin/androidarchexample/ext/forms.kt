package ru.kabylin.androidarchexample.ext

import android.util.Log
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.forms.Form

fun Form.setApiValidationErrors(errors: ApiValidationError) {
    errors.errors.forEach {
        Log.d("Test", "field: ${it.key}, message: ${it.value}")
        setErrorForField(it.key, it.value)
    }
}
