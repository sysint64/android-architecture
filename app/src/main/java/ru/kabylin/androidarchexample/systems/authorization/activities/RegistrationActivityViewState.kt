package ru.kabylin.androidarchexample.systems.authorization.activities

import ru.kabylin.androidarchexample.client.RequestStateListener
import ru.kabylin.androidarchexample.client.api.ApiError
import ru.kabylin.androidarchexample.client.api.ApiErrorListener
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.client.api.impl.ApiErrorCode
import ru.kabylin.androidarchexample.common.tools.watchers.TextWatcherChanged

import ru.kabylin.androidarchexample.views.ViewStateHolder

enum class LoadingState {
    PENDING,
    FINISHED
}

class RegistrationActivityViewState(private val viewStateHolder: ViewStateHolder)
    : RequestStateListener, ApiValidationErrorListener, ApiErrorListener
{
    enum class ScreenTransition {
        NONE,
        VERIFY_BY_SMS,
        FINISH_REGISTRATION
    }

    data class ViewStateData(
        var phone: String = "",
        var fromErrors: Map<String, String> = HashMap(),
        var authErrorMessage: String? = null,  // TODO: Int, string res
        var loadingState: LoadingState = LoadingState.FINISHED,
        var screenTransition: ScreenTransition = ScreenTransition.NONE
    )

    val data = ViewStateData()

    val phoneInputWatcher = object : TextWatcherChanged() {
        override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
            data.phone = sequence.toString()
        }
    }

    override fun onApiError(requestCode: Int, error: ApiError) {
        when (error.code) {
            ApiErrorCode.AUTHENTICATION_FAILED ->
                data.authErrorMessage = "Ошибка авторизации!"

            else -> {
                data.authErrorMessage = "Неизвестная ошибка!"
            }
        }

        viewStateHolder.viewStateEdenUpdate()
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        data.fromErrors = errors.errors
        viewStateHolder.viewStateEdenUpdate();
    }

    override fun onStartRequest(requestCode: Int) {
        data.loadingState = LoadingState.PENDING
        viewStateHolder.viewStateEdenUpdate();
    }

    override fun onFinishRequest(requestCode: Int) {
        data.loadingState = LoadingState.FINISHED
        viewStateHolder.viewStateEdenUpdate();
    }
}
