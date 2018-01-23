package ru.kabylin.androidarchexample.ext

import android.content.Context
import ru.kabylin.androidarchexample.tools.DialogTool

fun Context.alert(title: String, message: String,
                  positive: (() -> Unit)? = null, negative: (() -> Unit)? = null)
{
    DialogTool.alert(this, title, message, positive, negative)
}

fun Context.alert(title: String, message: String) {
    DialogTool.alert(this, title, message, null, null)
}
