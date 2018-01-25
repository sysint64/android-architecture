package ru.kabylin.androidarchexample.systems.authorization

import android.content.Context
import android.content.Intent
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

            // Route
            requestRegistrationState == RequestState.SUCCESS -> {
                dataStore.transitToScreen = Screen.VERIFY_BY_SMS
                requestRegistrationState = RequestState.IDLE
            }

            registrationAction == RegistrationAction.FINISH_REGISTRATION ->
                dataStore.transitToScreen = Screen.FINISH_REGISTRATION

            else -> { /* Avoid error */ }
        }
    }

    // Route
    routeActivity(context, viewHolder, dataStore)
    routeFragment(context, dataStore)

    // Update state
    viewHolder.viewStateFullUpdate()
}

private fun routeActivity(context: Context, viewHolder: ViewStateHolder, dataStore: DataStore) {
    if (viewHolder.viewStateInBackground())
        return

    if (dataStore.currentScreen == dataStore.transitToScreen)
        return

    val intent = Intent(context, dataStore.transitToScreen.cls.java)
    context.startActivity(intent)

    dataStore.currentScreen = dataStore.transitToScreen
}

private fun routeFragment(context: Context, dataStore: DataStore) {
//    if (!dataStore.transitToScreen.isFragment)
//        return
//
//    if (dataStore.currentScreen == dataStore.transitToScreen)
//        return
//
//    dataStore.currentScreen = dataStore.transitToScreen
}
