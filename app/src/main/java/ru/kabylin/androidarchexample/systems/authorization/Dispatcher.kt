package ru.kabylin.androidarchexample.systems.authorization

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import io.reactivex.rxkotlin.subscribeBy
import ru.kabylin.androidarchexample.*
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService

enum class DispatchAction {
    REQUEST_REGISTRATION,
    FINISH_REGISTRATION
}

fun dispatch(context: Context, dataStore: DataStore, action: DispatchAction, service: RegistrationService) {
    when (action) {
        DispatchAction.REQUEST_REGISTRATION -> {
            dataStore.registrationViewStateData.requestRegistrationState = RequestState.PROCESS
            sendEvent(context, Event.DATA_STORE_UPDATED)

            service.requestRegistration(dataStore.registrationViewStateData.phone)
                .subscribeBy(
                    onSuccess = {
                        dataStore.registrationViewStateData.requestRegistrationState = RequestState.SUCCESS
                        dataStore.registrationViewStateData.screenTransition = ScreenTransition.ACTIVITY_VERIFY_BY_SMS
                        sendEvent(context, Event.DATA_STORE_UPDATED)
                    },
                    onError = {
                        dataStore.registrationViewStateData.requestRegistrationState = RequestState.ERROR
                    }
                )
        }
        DispatchAction.FINISH_REGISTRATION -> {
            TODO("Not implemented")
        }
    }

    sendEvent(context, Event.DATA_STORE_UPDATED)
}

fun sendEvent(context: Context, event: Event) {
    val intent = Intent(DATA_STORE_BROADCAST_ACTION)
    intent.putExtra("event", event)
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
}
