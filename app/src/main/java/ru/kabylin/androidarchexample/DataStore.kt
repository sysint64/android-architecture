package ru.kabylin.androidarchexample

import android.support.annotation.StringRes
import ru.kabylin.androidarchexample.systems.authorization.RegistrationAction
import ru.kabylin.androidarchexample.systems.authorization.activities.FinishRegistrationActivity
import ru.kabylin.androidarchexample.systems.authorization.activities.RegistrationActivity
import ru.kabylin.androidarchexample.systems.authorization.activities.VerifyBySmsActivity
import kotlin.reflect.KClass

interface DataStoreAware {
    val dataStore: DataStore
}

enum class LoadingState {
    PENDING,
    FINISHED
}

enum class RequestState {
    IDLE,
    PROCESS,
    SUCCESS,
    ERROR
}

enum class Screen(val cls: KClass<*>, val isFragment: Boolean = false) {
    SPLASH(SplashActivity::class),
    REGISTRATION(RegistrationActivity::class),
    VERIFY_BY_SMS(VerifyBySmsActivity::class),
    FINISH_REGISTRATION(FinishRegistrationActivity::class),
}

data class RegistrationViewStateData(
    var phone: String = "",
    var fromErrors: Map<String, String> = HashMap(),
    @StringRes var authErrorMessage: Int = 0,
    var loadingState: LoadingState = LoadingState.FINISHED,
    var requestRegistrationState: RequestState = RequestState.IDLE,
    var registrationAction: RegistrationAction = RegistrationAction.IDLE
)

object DataStore {
    var transitToScreen: Screen = Screen.REGISTRATION
    var currentScreen: Screen = Screen.REGISTRATION
    val registrationViewStateData = RegistrationViewStateData()
}
