package ru.kabylin.androidarchexample.systems.authorization.activities

import android.annotation.SuppressLint
import kotlinx.android.synthetic.local.activity_registration_debug.*
import kotlinx.android.synthetic.main.activity_registration.*
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.forms.Form

@SuppressLint("SetTextI18n")
class DebugRegistrationActivityDelegate : RegistrationActivity.Delegate {
    override fun attachForm(form: Form) {
    }

    private lateinit var activity: RegistrationActivity

    override fun setContentView(activity: RegistrationActivity) {
        this.activity = activity
        this.activity.setContentView(R.layout.activity_registration_debug)
        init()
    }

    private fun init() {
        with(activity) {
            debugLoginButton.setOnClickListener {
                phoneInput.setText("+79137698347")
            }
        }
    }
}
