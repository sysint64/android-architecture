package ru.kabylin.androidarchexample.systems.authorization.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.client.RequestStateListener
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.common.ext.setErrors
import ru.kabylin.androidarchexample.systems.authorization.presenters.RegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.views.RegistrationView
import ru.kabylin.androidarchexample.forms.Form
import ru.kabylin.androidarchexample.forms.fields.editText
import ru.kabylin.androidarchexample.forms.form
import ru.kabylin.androidarchexample.forms.validators.PhoneValidator
import ru.kabylin.androidarchexample.forms.validators.RequiredValidator
import ru.kabylin.androidarchexample.systems.authorization.presenters.LoadingState
import ru.kabylin.androidarchexample.systems.authorization.presenters.TransitionState

class RegistrationActivity : KodeinAppCompatActivity(), RegistrationView {
    interface Delegate {
        fun onCreate(
            activity: RegistrationActivity,
            presenter: RegistrationPresenter
        )

        fun attachForm(form: Form)
    }

    private val presenter = RegistrationPresenter(injector)
    private val delegate: Delegate? by with(this).instanceOrNull()
    private lateinit var registrationForm: Form

    companion object {
        private const val REQUEST_FOR_RESULT_VERIFY_SMS = 1001
    }

    override fun provideOverridingModule() = Kodein.Module {
        bind<RequestStateListener>() with instance(presenter)
        bind<ApiValidationErrorListener>() with instance(presenter)
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
            onSubmit { presenter.requestRegistration() }
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("phone", phoneInput.text.toString())
            startActivity(intent)
        }

        delegate?.attachForm(registrationForm)
        render()

        phoneInput.setText("+7")
        progressBar.visibility = View.GONE
        phoneInput.addTextChangedListener(presenter.phoneInputWatcher)
    }

    override fun render() {
        with(presenter.viewData) {
            phoneInput.setText(phone)
            registrationForm.setErrors(fromErrors)

            // TODO: remove
            if (transitionState != TransitionState.REGISTRATION_ACTIVITY) {
                transitionState.transit(this@RegistrationActivity)
            }

            when (loadingState) {
                LoadingState.FINISHED -> {
                    registrationForm.disable()
                    alreadyRegisteredTextView.visibility = View.GONE
                    loginButton.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                LoadingState.PENDING -> {
                    registrationForm.enable()
                    alreadyRegisteredTextView.visibility = View.VISIBLE
                    loginButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
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

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }
}
