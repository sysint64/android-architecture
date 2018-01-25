package ru.kabylin.androidarchexample

import android.content.Context
import android.content.Intent
import ru.kabylin.androidarchexample.systems.authorization.activities.VerifyBySmsActivity

object Router {
    fun viewStateTransitionUpdate(context: Context, screenTransition: ScreenTransition) {
        when (screenTransition) {
            ScreenTransition.NONE -> {}
            ScreenTransition.ACTIVITY_VERIFY_BY_SMS -> {
                val intent = Intent(context, screenTransition.cls!!.java)
                context.startActivity(intent)
            }
            ScreenTransition.ACTIVITY_FINISH_REGISTRATION -> {
                val intent = Intent(context, screenTransition.cls!!.java)
                context.startActivity(intent)
            }
        }
    }
}
