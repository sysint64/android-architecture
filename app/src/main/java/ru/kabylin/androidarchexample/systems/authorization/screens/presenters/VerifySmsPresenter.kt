package ru.kabylin.androidarchexample.systems.authorization.screens.presenters

import io.reactivex.rxkotlin.subscribeBy
import ru.kabylin.androidarchexample.presenters.Presenter
import ru.kabylin.androidarchexample.systems.authorization.screens.views.VerifySmsView
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService

class VerifySmsPresenter(private val service: RegistrationService) : Presenter<VerifySmsView>() {
    companion object {
        val REQUEST_SERVICE_VERIFY_BY_SECRET_CODE = 2001
        val REQUEST_SERVICE_RETRY_SEND_SMS_CODE = 2002
    }

    lateinit var phone: String

    fun retrySendSmsCode() {
        service.withRequestCode(REQUEST_SERVICE_RETRY_SEND_SMS_CODE)
        service.requestRegistration(phone)
            .subscribeBy(
                onSuccess = {
                    view?.onVerifyCodeSent()
                },
                onError = {}
            )
    }
}
