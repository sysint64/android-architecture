package ru.kabylin.androidarchexample

import android.content.Context
import com.github.salomonbrys.kodein.*
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.DebugClient
import ru.kabylin.androidarchexample.systems.authorization.debugAuthorizationModule

fun dependencies(context: Context): Kodein {
    val kodein by Kodein.lazy {
        bind<Client>() with singleton { DebugClient(delay = 5) }
        bind<ManagedSQLiteOpenHelper>() with provider {
            DebugSqlHelper(context)
        }

        import(debugAuthorizationModule)
    }

    return kodein
}
