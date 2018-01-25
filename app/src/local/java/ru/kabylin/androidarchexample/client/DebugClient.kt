package ru.kabylin.androidarchexample.client

import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instanceOrNull
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.kabylin.androidarchexample.client.api.ApiErrorException
import ru.kabylin.androidarchexample.client.api.handleApiError
import ru.kabylin.androidarchexample.common.Service
import java.util.concurrent.TimeUnit

class DebugClient(
    private val backgroundScheduler: Scheduler = Schedulers.io(),
    private val resultScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val delay: Int = 0
) : Client, AwaitableRequestStateListener by CountDownLatchAwaitableRequestStateListener() {
    override fun <T> compose(service: Service, single: Single<out T>, injector: KodeinInjector): Single<out T> {
        val requestStateListener: RequestStateListener? by injector.instanceOrNull()
        val criticalErrorListener: CriticalErrorListener? by injector.instanceOrNull()

        val requestCode = service.currentRequestCode()
        service.withRequestCode(Service.REQUEST_DEFAULT)

        requestStateListener?.onStartRequest(requestCode)
        onStartRequest(requestCode)

        return single
            .delay(delay.toLong(), TimeUnit.SECONDS)
            .subscribeOn(backgroundScheduler)
            .observeOn(resultScheduler)
            .doOnError { throwable ->
                when (throwable) {
                    is ApiErrorException ->
                        handleApiError(requestCode, throwable, injector)

                    else -> criticalErrorListener?.onCriticalError(throwable)
                }

                requestStateListener?.onFinishRequest(requestCode)
                onFinishRequest(requestCode)
            }
            .doOnSuccess {
                requestStateListener?.onFinishRequest(requestCode)
                onFinishRequest(requestCode)
            }
    }

    override fun <T> compose(service: Service, flowable: Flowable<out T>, injector: KodeinInjector): Flowable<out T> {
        return flowable.delay(delay.toLong(), TimeUnit.SECONDS)
    }
}
