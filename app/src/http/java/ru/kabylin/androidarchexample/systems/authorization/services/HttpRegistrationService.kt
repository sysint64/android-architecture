package ru.kabylin.androidarchexample.systems.authorization.services

import com.github.salomonbrys.kodein.KodeinInjector
import io.reactivex.Single
import retrofit2.http.*
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.createRetrofitService
import ru.kabylin.androidarchexample.client.credentials.Credentials
import ru.kabylin.androidarchexample.services.Service
import ru.kabylin.androidarchexample.systems.authorization.models.requests.ActivationCodeRequest
import ru.kabylin.androidarchexample.systems.authorization.models.requests.RegistrationRequest
import ru.kabylin.androidarchexample.systems.authorization.models.responses.RegistrationResponse
import ru.kabylin.androidarchexample.client.credentials.AuthToken
import ru.kabylin.androidarchexample.systems.authorization.models.CodeRequest

class HttpRegistrationService(
    val client: Client,
    val injector: KodeinInjector
) : RegistrationService {
    override var requestCode: Int = Service.REQUEST_DEFAULT

    interface RetrofitService {
        @POST("/api/activation_codes/")
        fun getActivationCode(
            @Body body: ActivationCodeRequest
        ): Single<String>

        @POST("/api/users/")
        fun registerUser(
            @Body body: RegistrationRequest
        ): Single<RegistrationResponse>
    }

    private val retrofitService: RetrofitService

    init {
        retrofitService = createRetrofitService(client, RetrofitService::class.java)
    }

    override fun requestRegistration(phone: String): Single<out String> {
        //код "0" является запросом на регистрацию в системе
        val single = retrofitService
            .getActivationCode(ActivationCodeRequest(phone, CodeRequest.REGISTRATION.code))

        return client.compose(this, single, injector)
    }

    override fun finishRegistration(
        password: String,
        secretCode: String,
        activationCode: String
    ): Single<out Credentials> {
        val registrationRequest = RegistrationRequest(
            password = password,
            secret_code = secretCode,
            activation_code = activationCode
        )

        val single = retrofitService
            .registerUser(registrationRequest)
            .map(::registrationResponseToStandard)

        return client.compose(this, single, injector)
    }
}

fun registrationResponseToStandard(registrationResponse: RegistrationResponse): AuthToken =
    AuthToken(
        accessToken = registrationResponse.access_token,
        refreshToken = registrationResponse.refresh_token
    )
