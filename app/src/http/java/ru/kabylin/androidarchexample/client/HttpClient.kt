package ru.kabylin.androidarchexample.client

import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instanceOrNull
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.kabylin.androidarchexample.BuildConfig
import ru.kabylin.androidarchexample.client.api.ApiErrorException
import ru.kabylin.androidarchexample.client.api.handleApiError
import ru.kabylin.androidarchexample.common.Service
import ru.kabylin.androidarchexample.prefs.StoredAccessTokenCredentials
import java.util.concurrent.TimeUnit

class HttpClient(
    private val backgroundScheduler: Scheduler = Schedulers.io(),
    private val resultScheduler: Scheduler = AndroidSchedulers.mainThread()
) : Client, AwaitableRequestStateListener by CountDownLatchAwaitableRequestStateListener() {
    val retrofit by lazy { createRetrofitInstance() }
    private val httpClient by lazy { createHttpClientInstance() }
    private var currentHttpListener: HttpListener? = null
    private var currentRequestCode: Int = Service.REQUEST_DEFAULT

    override fun <T> compose(service: Service, single: Single<out T>, injector: KodeinInjector): Single<out T> {
        val requestStateListener: RequestStateListener? by injector.instanceOrNull()
        val criticalErrorListener: CriticalErrorListener? by injector.instanceOrNull()
        val httpListener: HttpListener? by injector.instanceOrNull()

        currentHttpListener = httpListener
        currentRequestCode = service.currentRequestCode()

        val requestCode = currentRequestCode
        service.withRequestCode(Service.REQUEST_DEFAULT)

        requestStateListener?.onStartRequest(requestCode)
        onStartRequest(requestCode)

        return single
            .subscribeOn(backgroundScheduler)
            .observeOn(resultScheduler)
            .doOnError { throwable ->
                currentHttpListener = null

                when (throwable) {
                    is ApiErrorException ->
                        handleApiError(requestCode, throwable, injector)

                    is HttpException ->
                        handleHttpError(requestCode, throwable, injector)

                    else -> criticalErrorListener?.onCriticalError(throwable)
                }

                requestStateListener?.onFinishRequest(requestCode)
                onFinishRequest(requestCode)
            }
            .doOnSuccess {
                currentHttpListener = null
                requestStateListener?.onFinishRequest(requestCode)
                onFinishRequest(requestCode)
            }
    }

    override fun <T> compose(service: Service, flowable: Flowable<out T>, injector: KodeinInjector): Flowable<out T> {
        return flowable
    }

    private fun createHttpClientInstance(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .build()

            val requestBuilder = original.newBuilder()
                .addHeader("X-AUTH", StoredAccessTokenCredentials.accessToken)
                .url(url)

            val request = requestBuilder.build()
            val response = chain.proceed(request)

            // Получение данных из OkHttp3 ответа и передача их в httpListener
            currentHttpListener?.let {
                // TODO: Временное решение, кажется использовать source.request не самая лучшая идея
                val responseBody = response.body()
                val source = responseBody?.source()
                source?.request(java.lang.Long.MAX_VALUE) // Запрос полного тела запроса
                val buffer = source?.buffer()

                val headers = HashMap<String, String>()

                response.headers().names().forEach {
                    val value = response.headers()[it]

                    if (value != null)
                        headers[it] = value
                }

                val responseData = HttpResponse(
                    body = buffer?.clone()?.readByteArray(),
                    statusCode = response.code(),
                    message = response.message(),
                    headers = headers
                )

                it.onHttpResponse(currentRequestCode, responseData)
            }

            response
        }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        return httpClient.build()
    }

    private fun createRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(httpClient)
            .build()
    }
}
