package ru.kabylin.androidarchexample.systems.authorization.services

import com.github.salomonbrys.kodein.KodeinInjector
import io.reactivex.Single
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.api.apiValidationException
import ru.kabylin.androidarchexample.credentials.Credentials
import ru.kabylin.androidarchexample.credentials.DebugTokenCredentials
import ru.kabylin.androidarchexample.ext.mapRowParser
import ru.kabylin.androidarchexample.services.Service
import java.util.*

class DebugAuthService(
    val client: Client,
    val database: ManagedSQLiteOpenHelper,
    val injector: KodeinInjector
) : AuthService {
    override var requestCode: Int = Service.REQUEST_DEFAULT

    override fun login(phone: String, password: String): Single<out Credentials> {
        val single = Single.fromCallable {

            lateinit var credentials: DebugTokenCredentials

            database.use {
                select("User")
                    .whereSimple("phone = ?", phone)
                    .limit(1)
                    .exec {
                        parseSingle(mapRowParser {
                            val correctPassword = it["password"] as String

                            if (password != correctPassword)
                                throw apiValidationException("password", "wrong password")

                            credentials = DebugTokenCredentials(
                                phone = phone,
                                accessToken = it["accessToken"] as String,
                                refreshToken = it["refreshToken"] as String,
                                expiredTime = 10
                            )
                        })
                    }
            }

            credentials
        }

        return client.compose(this, single, injector)
    }

    override fun updateToken(phone: String, refreshToken: String): Single<out Credentials> {
        val newAccessToken = UUID.randomUUID().toString()
        database.use {
            update("User",
                "accessToken" to newAccessToken,
                "refreshToken" to ""
            ).whereSimple("phone = ?", phone)
        }

        val credentials = DebugTokenCredentials(
            phone = phone,
            accessToken = newAccessToken,
            refreshToken = refreshToken,
            expiredTime = 10
        )

        return Single.just(credentials)
    }

    override fun logout(phone: String): Single<out Unit> {
        database.use {
            update("User",
                "accessToken" to "",
                "refreshToken" to ""
            ).whereSimple("phone = ?", phone)
        }

        return Single.just(Unit)
    }
}
