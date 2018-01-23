package ru.kabylin.androidarchexample.presenters

import android.support.annotation.CallSuper

abstract class Presenter<T> {
    protected open var view: T? = null

    open fun onStart() {}

    @CallSuper
    open fun onResume(view: T) {
        this.view = view
    }

    @CallSuper
    open fun onPause() {
        this.view = null
    }
}
