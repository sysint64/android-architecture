package ru.kabylin.androidarchexample.forms

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ru.kabylin.androidarchexample.forms.fields.Field

open class Form(val context: Context, val container: ViewGroup? = null) {
    private val fields: MutableList<Field> = ArrayList()
    private var submitButton: Button? = null
    private var onSubmit: (() -> Unit)? = null

    fun addField(field: Field) {
        if (field in fields)
            return

        fields.add(field)
        field.form = this
    }

    fun inflate(parent: ViewGroup) {
        fields.forEach { it.inflate(parent) }
    }

    fun isValid(): Boolean {
        var result = true

        for (field in fields) {
            val fieldIsValid = field.isValid()
            result = result && fieldIsValid
        }

        return result
    }

    fun setErrorForField(field: String, error: String) {
        fields
            .filter { it.name == field }
            .forEach { it.setError(error) }
    }

    fun disable() {
        fields.forEach { it.isEnabled = false }
        submitButton?.isEnabled = false
    }

    fun enable() {
        fields.forEach { it.isEnabled = true }
        submitButton?.isEnabled = true
    }

    fun attachSubmitButton(submitButton: Button) {
        this.submitButton = submitButton

        if (this.onSubmit != null) {
            this.submitButton?.setOnClickListener {
                if (isValid()) {
                    this.onSubmit?.invoke()
                }
            }
        }
    }

    fun onSubmit(onSubmit: () -> Unit) {
        this.onSubmit = onSubmit

        if (this.submitButton != null) {
            this.submitButton?.setOnClickListener {
                if (isValid()) {
                    this.onSubmit?.invoke()
                }
            }
        }
    }

    fun show() {
        fields.forEach { it.setVisible(true) }
        submitButton?.visibility = View.VISIBLE
    }

    fun hide() {
        fields.forEach { it.setVisible(false) }
        submitButton?.visibility = View.INVISIBLE
    }
}

// DSL
fun Context.form(container: ViewGroup, init: (Form).() -> Unit): Form {
    val form = Form(this, container)
    form.init()
    return form
}

fun Context.form(init: (Form).() -> Unit): Form {
    val form = Form(this)
    form.init()
    return form
}
