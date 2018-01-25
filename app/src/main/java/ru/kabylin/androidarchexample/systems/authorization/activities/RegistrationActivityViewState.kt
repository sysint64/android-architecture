package ru.kabylin.androidarchexample.systems.authorization.activities

import ru.kabylin.androidarchexample.DataStore
import ru.kabylin.androidarchexample.LoadingState
import ru.kabylin.androidarchexample.RegistrationViewStateData
import ru.kabylin.androidarchexample.client.RequestStateListener
import ru.kabylin.androidarchexample.client.api.ApiError
import ru.kabylin.androidarchexample.client.api.ApiErrorListener
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.client.api.impl.ApiErrorCode
import ru.kabylin.androidarchexample.common.tools.watchers.TextWatcherChanged

import ru.kabylin.androidarchexample.views.ViewStateHolder


class RegistrationActivityViewState(
    private val viewStateHolder: ViewStateHolder,
    private val dataStore: DataStore
) : RequestStateListener, ApiValidationErrorListener, ApiErrorListener {

    val phoneInputWatcher = object : TextWatcherChanged() {
        override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {
            dataStore.registrationViewStateData.phone = sequence.toString()
        }
    }

    override fun onApiError(requestCode: Int, error: ApiError) {
        when (error.code) {
            ApiErrorCode.AUTHENTICATION_FAILED -> {
//                dataStore.registrationViewStateData.authErrorMessage = "Ошибка авторизации!"
            }
            else -> {
//                dataStore.registrationViewStateData.authErrorMessage = "Неизвестная ошибка!"
            }
        }

        viewStateHolder.viewStateEdenUpdate()
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        dataStore.registrationViewStateData.fromErrors = errors.errors
        viewStateHolder.viewStateEdenUpdate();
    }

    override fun onStartRequest(requestCode: Int) {
        dataStore.registrationViewStateData.loadingState = LoadingState.PENDING
        viewStateHolder.viewStateEdenUpdate();
    }

    override fun onFinishRequest(requestCode: Int) {
        dataStore.registrationViewStateData.loadingState = LoadingState.FINISHED
        viewStateHolder.viewStateEdenUpdate();
    }
}
