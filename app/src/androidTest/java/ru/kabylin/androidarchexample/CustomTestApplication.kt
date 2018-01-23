package ru.kabylin.androidarchexample

import android.app.Application
import com.github.salomonbrys.kodein.*
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.DebugClient
import ru.kabylin.androidarchexample.systems.authorization.debugAuthorizationModule
import ru.kabylin.androidarchexample.systems.dashboard.debugDashboardModule

class CustomTestApplication : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        bind<Client>() with singleton { DebugClient() }
        bind<ManagedSQLiteOpenHelper>() with provider {
            DebugSqlHelper(this@CustomTestApplication)
        }

        import(debugAuthorizationModule)
        import(debugDashboardModule)
    }
}
