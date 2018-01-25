package ru.kabylin.androidarchexample.systems.authorization

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import io.reactivex.rxkotlin.subscribeBy
import ru.kabylin.androidarchexample.*
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService
import ru.kabylin.androidarchexample.views.ViewStateHolder

enum class RegistrationAction {
    IDLE,
    REQUEST_REGISTRATION,
    FINISH_REGISTRATION
}

fun dispatch(injector: KodeinInjector, dataStore: DataStore) {
    val viewHolder: ViewStateHolder by injector.instance()

    when (dataStore.registrationViewStateData.registrationAction) {
        RegistrationAction.IDLE -> {}
        RegistrationAction.REQUEST_REGISTRATION -> {
            val service : RegistrationService by injector.instance()
            dataStore.registrationViewStateData.requestRegistrationState = RequestState.PROCESS

            service.requestRegistration(dataStore.registrationViewStateData.phone)
                .subscribeBy(
                    onSuccess = {
                        dataStore.registrationViewStateData.requestRegistrationState = RequestState.SUCCESS
                        dispatch(injector, dataStore)
                    },
                    onError = {
                        dataStore.registrationViewStateData.requestRegistrationState = RequestState.ERROR
                        dispatch(injector, dataStore)
                    }
                )
        }
        RegistrationAction.FINISH_REGISTRATION -> {
            TODO("Not implemented")
        }
    }

    viewHolder.viewStateFullUpdate()
}

fun sendEvent(context: Context, event: Event) {
    val intent = Intent(DATA_STORE_BROADCAST_ACTION)
    intent.putExtra("event", event)
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
}
