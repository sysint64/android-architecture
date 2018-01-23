package ru.kabylin.androidarchexample.systems.authorization.screens.presenters

import io.reactivex.rxkotlin.subscribeBy
import ru.kabylin.androidarchexample.presenters.Presenter
import ru.kabylin.androidarchexample.systems.authorization.screens.views.RegistrationView
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService

class RegistrationPresenter(private val service: RegistrationService) : Presenter<RegistrationView>() {
    companion object {
        val REQUEST_SERVICE_REQUEST_REGISTRATION = 2001
    }

    var secretCode: String? = null
    var phone: String? = null

    override fun onResume(view: RegistrationView) {
        super.onResume(view)

        secretCode?.let {
            view.onRequestedRegistration(it)
            secretCode = null
        }
    }

    fun requestRegistration(phone: String) {
        service.withRequestCode(REQUEST_SERVICE_REQUEST_REGISTRATION)
        service.requestRegistration(phone)
            .subscribeBy(
                onSuccess = {
                    view?.onRequestedRegistration(it)
                    this.secretCode = it
                },
                onError = {}
            )
    }
}
