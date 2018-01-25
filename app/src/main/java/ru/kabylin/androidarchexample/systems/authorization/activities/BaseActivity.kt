package ru.kabylin.androidarchexample.systems.authorization.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.annotation.CallSuper
import android.support.v4.content.LocalBroadcastManager
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import ru.kabylin.androidarchexample.*
import ru.kabylin.androidarchexample.views.ViewStateHolder

@SuppressLint("Registered")
open class BaseActivity : KodeinAppCompatActivity(), DataStoreAware, ViewStateHolder {
    override val dataStore = DataStore
    var isPaused = false
        private set

    private val dataStoreEventReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewStateFullUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
        isPaused = false

        LocalBroadcastManager.getInstance(this).registerReceiver(
            dataStoreEventReceiver,
            IntentFilter(DATA_STORE_BROADCAST_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        isPaused = true

        LocalBroadcastManager.getInstance(this).unregisterReceiver(dataStoreEventReceiver)
    }

    final override fun viewStateTransitionUpdate() {
        if (isPaused)
            return

        Router.viewStateTransitionUpdate(this, dataStore.registrationViewStateData.screenTransition)
    }

    @CallSuper
    override fun viewStateFullUpdate() {
        viewStateEdenUpdate()
        viewStateTransitionUpdate()
    }
}
