package ru.kabylin.androidarchexample

import android.support.annotation.StringRes
import ru.kabylin.androidarchexample.systems.authorization.activities.FinishRegistrationActivity
import ru.kabylin.androidarchexample.systems.authorization.activities.VerifyBySmsActivity
import kotlin.reflect.KClass

interface DataStoreAware {
    val dataStore: DataStore
}

enum class LoadingState {
    PENDING,
    FINISHED
}

enum class ScreenTransition(val cls: KClass<*>? = null) {
    NONE,
    ACTIVITY_VERIFY_BY_SMS(VerifyBySmsActivity::class),
    ACTIVITY_FINISH_REGISTRATION(FinishRegistrationActivity::class)
}

enum class Event {
    DATA_STORE_UPDATED
}

data class RegistrationViewStateData(
    var phone: String = "",
    var fromErrors: Map<String, String> = HashMap(),
    @StringRes var authErrorMessage: Int = 0,
    var loadingState: LoadingState = LoadingState.FINISHED,
    var screenTransition: ScreenTransition = ScreenTransition.NONE,
    var requestRegistrationState: RequestRegistrationState = RequestRegistrationState.IDLE
) {
    enum class RequestRegistrationState {
        IDLE,
        PROCESS,
        SUCCESS,
        ERROR
    }
}

const val DATA_STORE_BROADCAST_ACTION = "ru.kabylin.androidarchexample.DataStore.BroadcastAction"

object DataStore {
    val registrationViewStateData = RegistrationViewStateData()
}
