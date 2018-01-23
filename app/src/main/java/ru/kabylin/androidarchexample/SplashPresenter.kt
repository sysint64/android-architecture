package ru.kabylin.androidarchexample

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.kabylin.androidarchexample.ext.subscribeOnSuccess
import ru.kabylin.androidarchexample.presenters.Presenter
import java.util.concurrent.TimeUnit

class SplashPresenter : Presenter<SplashActivity>() {

    fun doRequest() {
        Single.just(1)
            .delay(2, TimeUnit.SECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOnSuccess(::onSuccessRequest)
    }

    private fun onSuccessRequest(result: Int) {
        // FIXME: Избавиться от повторного ожидания
        view?.toNextScreen()
    }
}
