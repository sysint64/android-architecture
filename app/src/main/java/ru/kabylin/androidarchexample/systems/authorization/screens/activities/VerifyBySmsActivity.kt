package ru.kabylin.androidarchexample.systems.authorization.screens.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import kotlinx.android.synthetic.main.activity_verify_by_sms.*
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.client.RequestStateListener
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.ext.setApiValidationErrors
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.VerifySmsPresenter
import ru.kabylin.androidarchexample.systems.authorization.screens.views.VerifySmsView
import ru.kabylin.androidarchexample.forms.Form
import ru.kabylin.androidarchexample.forms.fields.editText
import ru.kabylin.androidarchexample.forms.form
import ru.kabylin.androidarchexample.forms.validators.RequiredValidator
import java.util.*

class VerifyBySmsActivity : KodeinAppCompatActivity(), VerifySmsView, RequestStateListener, ApiValidationErrorListener {
    companion object {
        val SMS_VERIFY_RETRY_SECONDS = 10
    }

    private val presenter: VerifySmsPresenter by with(this).instance()
    private lateinit var verifyForm: Form

    private val timer = Timer()
    private var time = SMS_VERIFY_RETRY_SECONDS + 1

    override fun provideOverridingModule() = Kodein.Module {
        bind<RequestStateListener>() with instance(this@VerifyBySmsActivity)
        bind<ApiValidationErrorListener>() with instance(this@VerifyBySmsActivity)
        bind<KodeinInjector>() with instance(this@VerifyBySmsActivity.injector)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_by_sms)

        presenter.phone = intent.extras["phone"] as String

        verifyForm = form {
            editText(name = "verifyCode", validators = listOf(RequiredValidator())) {
                attach(
                    editText = smsCodeInput,
                    textInputLayout = smsCodeInputLayout
                )
            }

            attachSubmitButton(loginButton)
            onSubmit {
                val activatedCode = smsCodeInput.text.toString()
                val intent = Intent(this@VerifyBySmsActivity, FinishRegistrationActivity::class.java)

                with(intent) {
                    putExtra("activatedCode", activatedCode)
                    putExtra("secretCode", this@VerifyBySmsActivity.intent.getStringExtra("secretCode"))
                    putExtra("phone", presenter.phone)
                }

                startActivity(intent)
            }
        }

        progressBar.visibility = View.GONE
        startTimer()

        resendSmsButton.setOnClickListener {
            TODO("реализовать переотправку смс-кода")
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    private fun startTimer() {
        timerText.visibility = View.VISIBLE
        resendSmsButton.isEnabled = false

        timer.schedule(object : TimerTask() {
            override fun run() {
                time--

                if (time <= 0)
                    this.cancel()

                runOnUiThread { updateUITime() }
            }
        }, 0, 1000)
    }

    private fun updateUITime() {
        if (time <= 0) {
            timerText.text = getString(R.string.resend_sms_timer, time)
            timerText.visibility = View.GONE
            resendSmsButton.isEnabled = true
        } else {
            timerText.text = getString(R.string.resend_sms_timer, time)
        }
    }

    override fun onVerified() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onVerifyCodeSent() {
        time = SMS_VERIFY_RETRY_SECONDS + 1
        startTimer()
    }

    override fun onStartRequest(requestCode: Int) {
        verifyForm.disable()
        progressBar.visibility = View.VISIBLE
    }

    override fun onFinishRequest(requestCode: Int) {
        verifyForm.enable()
        progressBar.visibility = View.GONE
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        Log.d("VerifyBySmsActivity", errors.errors.toString())
        verifyForm.setApiValidationErrors(errors)
    }
}
