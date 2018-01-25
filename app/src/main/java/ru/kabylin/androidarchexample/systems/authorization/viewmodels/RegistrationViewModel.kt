package ru.kabylin.androidarchexample.systems.authorization.viewmodels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.Single
import ru.kabylin.androidarchexample.client.api.ApiError
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.systems.authorization.activities.VerifyBySmsActivity
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService
import kotlin.reflect.KClass

const val REQUEST_SERVICE_REQUEST_REGISTRATION = 2001

enum class TransitionState(val cls: KClass<*>? = null, var bundle: Bundle = Bundle()) {
    NONE,
    VERIFY_SMS_CODE(VerifyBySmsActivity::class);

    fun transit(context: Context) {
        if (cls != null) {
            val intent = Intent(context, cls.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}

data class RegistrationViewData(
    val phone: String = "",
    val transitionState: TransitionState = TransitionState.NONE,
    val validationErrors: ApiValidationError? = null,
    val commonError: String? = null
)

val registrationViewData = RegistrationViewData()

class RegistrationViewModel(private val service: RegistrationService) {
//    val requestRegistration = Flowable.fromCallable {
//        val bundle = Bundle()
//        service.requestRegistration(data.phone)
//            .subscribeOnSuccess {
//
//            }
//        Flowable.just(bundle)
//    }

    fun requestRegistration(phone: String): Single<Bundle> {
        service.withRequestCode(REQUEST_SERVICE_REQUEST_REGISTRATION)
        return service.requestRegistration(phone)
            .flatMap {
                val bundle = Bundle()
                Single.just(bundle)
            }
    }
}
