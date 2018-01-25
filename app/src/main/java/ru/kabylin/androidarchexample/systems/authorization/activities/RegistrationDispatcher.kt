package ru.kabylin.androidarchexample.systems.authorization.activities

import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import ru.kabylin.androidarchexample.common.ext.subscribeOnSuccess
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService
import ru.kabylin.androidarchexample.views.ViewStateHolder

class RegistrationDispatcher(
    private val viewStateHolder: ViewStateHolder,
    injector: KodeinInjector
) {
    companion object {
        const val REQUEST_SERVICE_REQUEST_REGISTRATION = 2001
    }

    private val service: RegistrationService by injector.instance()

    fun requestRegistration(viewState: RegistrationActivityViewState) {
        service.withRequestCode(REQUEST_SERVICE_REQUEST_REGISTRATION)
        service.requestRegistration(viewState.data.phone)
            .subscribeOnSuccess {
                viewState.data.screenTransition =
                    RegistrationActivityViewState.ScreenTransition.VERIFY_BY_SMS
                viewStateHolder.viewStateTransitionUpdate()
            }
    }

    fun openFinishRegistration(viewState: RegistrationActivityViewState) {
        viewState.data.screenTransition =
            RegistrationActivityViewState.ScreenTransition.FINISH_REGISTRATION
        viewStateHolder.viewStateTransitionUpdate()
    }
}
