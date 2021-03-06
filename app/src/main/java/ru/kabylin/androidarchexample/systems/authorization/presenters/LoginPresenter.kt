package ru.kabylin.androidarchexample.systems.authorization.presenters

import ru.kabylin.androidarchexample.common.ext.subscribeOnSuccess
import ru.kabylin.androidarchexample.prefs.StoredUserInfo
import ru.kabylin.androidarchexample.common.Presenter
import ru.kabylin.androidarchexample.systems.authorization.views.LoginView
import ru.kabylin.androidarchexample.systems.authorization.services.AuthService

class LoginPresenter(private val service: AuthService) : Presenter<LoginView>() {
    companion object {
        val REQUEST_LOGIN = 2001
        val TAG = "LoginPresenter"
    }

    fun login(phone: String, password: String) {
        service.withRequestCode(REQUEST_LOGIN)
        service.login(phone, password)
            .subscribeOnSuccess { credentials ->
                credentials.store()
                StoredUserInfo.phone = phone
                view?.onSignedIn()
            }
    }
}
