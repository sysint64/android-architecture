package ru.kabylin.androidarchexample.systems.authorization.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.annotation.CallSuper
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import ru.kabylin.androidarchexample.*
import ru.kabylin.androidarchexample.systems.authorization.dispatch
import ru.kabylin.androidarchexample.views.ViewStateHolder

@SuppressLint("Registered")
abstract class BaseActivity : KodeinAppCompatActivity(), DataStoreAware, ViewStateHolder {
    override val dataStore = DataStore
    var isPaused = false
        private set

    override fun viewStateInBackground() = isPaused

    private val dataStoreEventReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewStateFullUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        dispatch(this, injector, dataStore)
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    @CallSuper
    override fun viewStateFullUpdate() {
        viewStateEdenUpdate()
    }
}
