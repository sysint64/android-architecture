package ru.kabylin.androidarchexample.client

import com.github.salomonbrys.kodein.KodeinInjector
import io.reactivex.Flowable
import io.reactivex.Single
import ru.kabylin.androidarchexample.common.Service

interface Client : AwaitableRequest {
    fun <T> compose(service: Service, single: Single<out T>, injector: KodeinInjector): Single<out T>

    fun <T> compose(service: Service, flowable: Flowable<out T>, injector: KodeinInjector): Flowable<out T>
}
