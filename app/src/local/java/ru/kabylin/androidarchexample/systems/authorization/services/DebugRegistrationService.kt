package ru.kabylin.androidarchexample.systems.authorization.services

import com.github.salomonbrys.kodein.KodeinInjector
import io.reactivex.Single
import org.jetbrains.anko.db.*
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.api.apiValidationException
import ru.kabylin.androidarchexample.credentials.DebugTokenCredentials
import ru.kabylin.androidarchexample.services.Service
import ru.kabylin.androidarchexample.credentials.Credentials
import java.util.*

class DebugRegistrationService(
    val client: Client,
    val database: ManagedSQLiteOpenHelper,
    val injector: KodeinInjector
) : RegistrationService {
    override var requestCode: Int = Service.REQUEST_DEFAULT

    // Ключ - номер телефона, значение - секретный код
    companion object {
        //Ключ - номер телефона, значение - код активации
        private val activationCodes: MutableMap<String, String> = HashMap()
        //Ключ - номер телефона, значение - секретный код
        private val secretCodes: MutableMap<String, String> = HashMap()
    }

    override fun requestRegistration(phone: String): Single<out String> {
        val single = Single.fromCallable {
            activationCodes[phone] = "1111"
            val secretCode = UUID.randomUUID().toString()
            secretCodes[phone] = secretCode

            secretCode
        }

        return client.compose(this, single, injector)
    }

    override fun finishRegistration(
        password: String,
        secretCode: String,
        activationCode: String
    ): Single<out Credentials> {
        val single = Single.fromCallable {

            var phone = ""
            secretCodes.filter { it.value == secretCode }.forEach { phone = it.key }
            val thereIsNotSecreteCode = phone.trim().isEmpty()

            if (thereIsNotSecreteCode)
                throw apiValidationException("secret_code", "wrong secret code")

            if (activationCodes[phone] != activationCode)
                throw apiValidationException("activation", "wrong activation code")

            val accessToken = UUID.randomUUID().toString()
            val refreshToken = UUID.randomUUID().toString()

            database.use {
                insert("User",
                    "phone" to phone,
                    "password" to password,
                    "accessToken" to accessToken,
                    "bonuses" to 0,
                    "refreshToken" to refreshToken
                )
            }

            DebugTokenCredentials(
                phone = phone,
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiredTime = 10
            )
        }

        return single
    }
}
