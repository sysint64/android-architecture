package ru.kabylin.androidarchexample.systems.authorization.presenters

import io.reactivex.rxkotlin.subscribeBy
import ru.kabylin.androidarchexample.prefs.Settings
import ru.kabylin.androidarchexample.common.Presenter
import ru.kabylin.androidarchexample.systems.authorization.views.FinishRegistrationView
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService

class FinishRegistrationPresenter(
    private val registrationService: RegistrationService
): Presenter<FinishRegistrationView>() {
    companion object {
        val REQUEST_SERVICE_FINISH_REGISTRATION = 2001
    }

    fun finishRegistration(password: String, secretCode: String, activationCode: String) {
        registrationService.withRequestCode(REQUEST_SERVICE_FINISH_REGISTRATION)
        registrationService.finishRegistration(password, secretCode, activationCode).
            subscribeBy(
                onSuccess = {
                    Settings.passwordWasSet = true
                    it.store()
                    view?.onRegistrationFinished(it)
                },
                onError = {}
            )
    }
}
