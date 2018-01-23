package ru.kabylin.androidarchexample.ext

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun <T> Single<T>.subscribeOnSuccess(onSuccess: (T) -> Unit) =
    subscribe(onSuccess, {})

private infix fun Disposable.addTo(disposables: CompositeDisposable) {
    disposables.add(this)
}
