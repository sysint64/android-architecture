package ru.kabylin.androidarchexample.systems.authorization.activities

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
import ru.kabylin.androidarchexample.common.ext.subscribeOnSuccess
import ru.kabylin.androidarchexample.forms.Form
import ru.kabylin.androidarchexample.forms.fields.editText
import ru.kabylin.androidarchexample.forms.form
import ru.kabylin.androidarchexample.forms.validators.PhoneValidator
import ru.kabylin.androidarchexample.forms.validators.RequiredValidator
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService
import ru.kabylin.androidarchexample.views.ViewStateHolder

class RegistrationActivity : KodeinAppCompatActivity(), ViewStateHolder {
    interface Delegate {
        fun setContentView(activity: RegistrationActivity)

        fun attachForm(form: Form)
    }

    companion object {
        private const val REQUEST_FOR_RESULT_VERIFY_SMS = 1001
    }

    private val viewState = RegistrationActivityViewState(this)
    private val delegate: Delegate? by injector.instanceOrNull()
    private var inBackground = false
    private val service: RegistrationService by injector.instance()

    private lateinit var registrationForm: Form

    override fun provideOverridingModule() = Kodein.Module {
        bind<RequestStateListener>() with instance(viewState)
        bind<ApiValidationErrorListener>() with instance(viewState)
        bind<KodeinInjector>() with instance(this@RegistrationActivity.injector)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate?.setContentView(this) ?: setContentView(R.layout.activity_registration)

        registrationForm = form {
            editText(name = "phone", validators = listOf(RequiredValidator(), PhoneValidator())) {
                attach(
                    editText = phoneInput,
                    textInputLayout = phoneInputLayout
                )
            }

            attachSubmitButton(registerButton)
            onSubmit {
                service.requestRegistration(viewState.data.phone)
                    .subscribeOnSuccess {
                        viewState.data.screenTransition =
                            RegistrationActivityViewState.ScreenTransition.VERIFY_BY_SMS
                        viewStateTransitionUpdate()
                    }
            }
        }

        phoneInput.addTextChangedListener(viewState.phoneInputWatcher)
        delegate?.attachForm(registrationForm)
    }

    override fun viewStateEdenUpdate() {
        with(viewState.data) {
            registrationForm.setErrors(fromErrors)

            when (loadingState) {
                LoadingState.FINISHED -> {
                    registrationForm.enable()
                    alreadyRegisteredTextView.visibility = View.VISIBLE
                    loginButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                LoadingState.PENDING -> {
                    registrationForm.disable()
                    alreadyRegisteredTextView.visibility = View.GONE
                    loginButton.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun viewStateFullUpdate() {
        viewStateEdenUpdate()
        phoneInput.setText(viewState.data.phone)
        viewStateTransitionUpdate()
    }

    override fun viewStateTransitionUpdate() {
        if (inBackground)
            return

        when (viewState.data.screenTransition) {
            RegistrationActivityViewState.ScreenTransition.VERIFY_BY_SMS -> {
                val intent = Intent(this, VerifyBySmsActivity::class.java)
                intent.putExtra("phone", viewState.data.phone)
                startActivity(intent)
            }
            RegistrationActivityViewState.ScreenTransition.FINISH_REGISTRATION -> {
                val intent = Intent(this, FinishRegistrationActivity::class.java)
                startActivity(intent)
            }
            else -> {}
        }

        viewState.data.screenTransition = RegistrationActivityViewState.ScreenTransition.NONE
    }

    override fun onResume() {
        super.onResume()
        inBackground = false
        viewStateFullUpdate()
    }

    override fun onPause() {
        super.onPause()
        inBackground = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        when (requestCode) {
            REQUEST_FOR_RESULT_VERIFY_SMS -> {
                viewState.data.screenTransition =
                    RegistrationActivityViewState.ScreenTransition.FINISH_REGISTRATION
                viewStateTransitionUpdate()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
