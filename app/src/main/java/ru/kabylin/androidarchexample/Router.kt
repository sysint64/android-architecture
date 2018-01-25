package ru.kabylin.androidarchexample

import android.content.Context
import android.content.Intent

object Router {
    fun viewStateTransitionUpdate(context: Context, screenTransition: ScreenTransition) {
        if (screenTransition == ScreenTransition.NONE)
            return

        val intent = Intent(context, screenTransition.cls!!.java)
        context.startActivity(intent)
    }
}
