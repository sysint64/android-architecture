package ru.kabylin.androidarchexample.common.tools.watchers

import android.text.Editable
import android.text.TextWatcher

abstract class TextWatcherChanged : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}

class StringTextWatcher(private var str: String) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        str = s.toString()
    }
}
