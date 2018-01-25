package ru.kabylin.androidarchexample.common.tools

import android.app.AlertDialog
import android.content.Context


object DialogTool {
    fun alert(context: Context, title: String, message: String,
              positive: (() -> Unit)? = null, negative: (() -> Unit)? = null) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", { _, _ -> positive?.invoke() })
            .setNegativeButton("Cancel", { _, _ -> negative?.invoke() })
            .show()
    }
}
