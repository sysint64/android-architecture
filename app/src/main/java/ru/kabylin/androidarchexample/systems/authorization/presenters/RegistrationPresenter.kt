package ru.kabylin.androidarchexample.systems.authorization.presenters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import ru.kabylin.androidarchexample.client.RequestStateListener
import ru.kabylin.androidarchexample.client.api.ApiError
import ru.kabylin.androidarchexample.client.api.ApiErrorListener
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.client.api.impl.ApiErrorCode
import ru.kabylin.androidarchexample.common.Presenter
import ru.kabylin.androidarchexample.common.ext.subscribeOnSuccess
import ru.kabylin.androidarchexample.common.tools.watchers.StringTextWatcher
import ru.kabylin.androidarchexample.systems.authorization.activities.VerifyBySmsActivity
import ru.kabylin.androidarchexample.systems.authorization.views.RegistrationView
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService
import kotlin.reflect.KClass

// TODO: remove
enum class TransitionState(val cls: KClass<*>? = null, var bundle: Bundle = Bundle()) {
    REGISTRATION_ACTIVITY,
    VERIFY_SMS_CODE(VerifyBySmsActivity::class);

    fun transit(context: Context) {
        if (cls != null) {
            val intent = Intent(context, cls.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}

enum class LoadingState {
    PENDING,
    FINISHED
}

data class RegistrationViewState(
    var phone: String = "",  // +
    var transitionState: TransitionState = TransitionState.REGISTRATION_ACTIVITY,  // TODO: rm
    var fromErrors: Map<String, String> = HashMap(),  // +
    var authErrorMessage: String? = null,  // TODO: Int, string res
    var loadingState: LoadingState = LoadingState.FINISHED  // +
)

const val REQUEST_SERVICE_REQUEST_REGISTRATION = 2001

class RegistrationPresenter(injector: KodeinInjector)
    : Presenter<RegistrationView>(), RequestStateListener, ApiValidationErrorListener,
    ApiErrorListener
{
    private val service: RegistrationService by injector.instance()
    val viewData = RegistrationViewState()
    val phoneInputWatcher = StringTextWatcher(viewData.phone)

    fun requestRegistration() {
        service.withRequestCode(REQUEST_SERVICE_REQUEST_REGISTRATION)
        service.requestRegistration(viewData.phone)
            .subscribeOnSuccess {
                with(viewData) {
                    transitionState = TransitionState.VERIFY_SMS_CODE
                    transitionState.bundle.putString("phone", phone)
                    transitionState.bundle.putString("secretCode", it)
                    view?.render()
                }
            }
    }

    override fun onStartRequest(requestCode: Int) {
        viewData.loadingState = LoadingState.PENDING
        view?.render()
    }

    override fun onFinishRequest(requestCode: Int) {
        viewData.loadingState = LoadingState.FINISHED
        view?.render()
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        viewData.fromErrors = errors.errors
        view?.render()
    }

    override fun onApiError(requestCode: Int, error: ApiError) {
        when (error.code) {
            ApiErrorCode.AUTHENTICATION_FAILED ->
                viewData.authErrorMessage = "Ошибка авторизации!"

            else -> {}
        }

        view?.render()
    }
}
