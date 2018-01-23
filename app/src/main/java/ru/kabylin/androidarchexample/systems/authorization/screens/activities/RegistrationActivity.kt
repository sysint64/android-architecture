package ru.kabylin.androidarchexample.systems.authorization.screens.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.client.*
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.ext.alert
import ru.kabylin.androidarchexample.ext.setApiValidationErrors
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.RegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.screens.views.RegistrationView
import ru.kabylin.androidarchexample.forms.Form
import ru.kabylin.androidarchexample.forms.fields.editText
import ru.kabylin.androidarchexample.forms.form
import ru.kabylin.androidarchexample.forms.validators.PhoneValidator
import ru.kabylin.androidarchexample.forms.validators.RequiredValidator

class RegistrationActivity : KodeinAppCompatActivity(), RegistrationView, RequestStateListener,
    ApiValidationErrorListener, CriticalErrorListener, AccessErrorListener
{
    interface Delegate {
        fun onCreate(
            activity: RegistrationActivity,
            presenter: RegistrationPresenter
        )

        fun attachForm(form: Form)
    }

    private val presenter: RegistrationPresenter by with(this).instance()
    private val delegate: Delegate? by with(this).instanceOrNull()
    private lateinit var registrationForm: Form
    private var phone = ""

    companion object {
        private val REQUEST_FOR_RESULT_VERIFY_SMS = 1001
    }

    override fun provideOverridingModule() = Kodein.Module {
        bind<RequestStateListener>() with instance(this@RegistrationActivity)
        bind<ApiValidationErrorListener>() with instance(this@RegistrationActivity)
        bind<CriticalErrorListener>() with instance(this@RegistrationActivity)
        bind<KodeinInjector>() with instance(this@RegistrationActivity.injector)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        delegate?.onCreate(
            activity = this,
            presenter = presenter
        ) ?: setContentView(R.layout.activity_registration)

        registrationForm = form {
            editText(name = "phone", validators = listOf(RequiredValidator(), PhoneValidator())) {
                attach(
                    editText = phoneInput,
                    textInputLayout = phoneInputLayout
                )
            }

            // TODO: Подумать о смене интерфейса, к примеру так: onSubmit(registerButton)
            attachSubmitButton(registerButton)
            onSubmit { presenter.requestRegistration(phoneInput.text.toString()) }
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("phone", phoneInput.text.toString())
            startActivity(intent)
        }

        delegate?.attachForm(registrationForm)
        presenter.onStart()
        phoneInput.setText("+7")
        progressBar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onRequestedRegistration(secretCode: String) {
        phone = phoneInput.text.toString()
        intent = Intent(this, VerifyBySmsActivity::class.java)
        intent.putExtra("phone", phone)
        intent.putExtra("secretCode", secretCode)
        startActivityForResult(intent, REQUEST_FOR_RESULT_VERIFY_SMS)
    }

    override fun onStartRequest(requestCode: Int) {
        registrationForm.disable()
        alreadyRegisteredTextView.visibility = View.GONE
        loginButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onFinishRequest(requestCode: Int) {
        registrationForm.enable()
        alreadyRegisteredTextView.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        registrationForm.setApiValidationErrors(errors)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        when (requestCode) {
            REQUEST_FOR_RESULT_VERIFY_SMS -> {
                val intent = Intent(this, FinishRegistrationActivity::class.java)
                startActivity(intent)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCriticalError(throwable: Throwable) {
        alert("ClientError", throwable.message ?: "Critical error!")
    }

    override fun notAuthenticatedError(requestCode: Int, error: ClientError) {
    }

    override fun permissionDeniedError(requestCode: Int, error: ClientError) {
    }

    override fun throttledError(requestCode: Int, error: ClientError) {
    }
}
