package ru.kabylin.androidarchexample.systems.authorization.services

import com.github.salomonbrys.kodein.KodeinInjector
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.createRetrofitService
import ru.kabylin.androidarchexample.credentials.Credentials
import ru.kabylin.androidarchexample.services.Service
import ru.kabylin.androidarchexample.systems.authorization.models.requests.LoginRequest
import ru.kabylin.androidarchexample.systems.authorization.models.requests.UpdateTokenRequest
import ru.kabylin.androidarchexample.systems.authorization.models.responses.LoginResponse
import ru.kabylin.androidarchexample.systems.authorization.models.responses.UpdateTokenResponse
import ru.kabylin.androidarchexample.systems.authorization.models.toCredentials

class HttpAuthService(
    val client: Client,
    val injector: KodeinInjector
) : AuthService {
    override var requestCode: Int = Service.REQUEST_DEFAULT

    interface RetrofitService {
        @POST("/api/users/{phone}/login/")
        fun login(
            @Path("phone") phone: String,
            @Body body: LoginRequest
        ): Single<LoginResponse>

        @POST("/api/users/{phone}/token_refresh/")
        fun updateToken(
            @Path("phone") phone: String,
            @Body body: UpdateTokenRequest
        ): Single<UpdateTokenResponse>

        @POST("/api/users/{phone}/logout/")
        fun logout(
            @Path("phone") phone: String
        ): Single<Response<Unit>>
    }

    private val retrofitService: RetrofitService

    init {
        retrofitService = createRetrofitService(client, RetrofitService::class.java)
    }

    override fun login(phone: String, password: String): Single<out Credentials> {
        val single = retrofitService
            .login(phone, LoginRequest(password))
            .map { it.toCredentials() }

        return client.compose(this, single, injector)
    }

    override fun updateToken(phone: String, refreshToken: String): Single<out Credentials> {
        val single = retrofitService
            .updateToken(phone, UpdateTokenRequest(refreshToken))
            .map { it.toCredentials() }

        return client.compose(this, single, injector)
    }

    override fun logout(phone: String): Single<out Unit> {
        val single = retrofitService
            .logout(phone)
            .map { Unit }

        return client.compose(this, single, injector)
    }
}
