package ru.kabylin.androidarchexample.systems.authorization

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import io.reactivex.rxkotlin.subscribeBy
import ru.kabylin.androidarchexample.*
import ru.kabylin.androidarchexample.systems.authorization.activities.FinishRegistrationActivity
import ru.kabylin.androidarchexample.systems.authorization.activities.VerifyBySmsActivity
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService
import ru.kabylin.androidarchexample.views.ViewStateHolder

enum class RegistrationAction {
    IDLE,
    REQUEST_REGISTRATION,
    FINISH_REGISTRATION
}

fun dispatch(context: Context, injector: KodeinInjector, dataStore: DataStore) {
    val viewHolder: ViewStateHolder by injector.instance()

    with(dataStore.registrationViewStateData) {
        when {
            registrationAction == RegistrationAction.REQUEST_REGISTRATION -> {
                val service: RegistrationService by injector.instance()
                requestRegistrationState = RequestState.PROCESS

                service.requestRegistration(phone)
                    .subscribeBy(
                        onSuccess = {
                            requestRegistrationState = RequestState.SUCCESS
                            registrationAction = RegistrationAction.IDLE
                            dispatch(context, injector, dataStore)
                        },
                        onError = {
                            requestRegistrationState = RequestState.ERROR
                            registrationAction = RegistrationAction.IDLE
                            dispatch(context, injector, dataStore)
                        }
                    )
            }
            requestRegistrationState == RequestState.SUCCESS -> {
                if (viewHolder.viewStateInBackground()) {
                    return@with
                }

                requestRegistrationState = RequestState.IDLE
                val intent = Intent(context, VerifyBySmsActivity::class.java)
                context.startActivity(intent)
            }
            registrationAction == RegistrationAction.REQUEST_REGISTRATION -> {
                val intent = Intent(context, FinishRegistrationActivity::class.java)
                context.startActivity(intent)

                if (viewHolder.viewStateInBackground()) {
                    return@with
                }
            }
            else -> {}
        }
    }

    viewHolder.viewStateFullUpdate()
}

fun sendEvent(context: Context, event: Event) {
    val intent = Intent(DATA_STORE_BROADCAST_ACTION)
    intent.putExtra("event", event)
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
}
