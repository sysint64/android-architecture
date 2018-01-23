package ru.kabylin.androidarchexample

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.github.salomonbrys.kodein.*
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.robolectric.RuntimeEnvironment
import org.robolectric.TestLifecycleApplication
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.DebugClient
import ru.kabylin.androidarchexample.systems.authorization.debugAuthorizationModule
import ru.kabylin.androidarchexample.systems.dashboard.debugDashboardModule
import java.lang.reflect.Method

class TestMainApplication : Application(), KodeinAware, TestLifecycleApplication {
    override val kodein by Kodein.lazy {
        bind<KodeinInjector>() with provider {
            val injector = KodeinInjector()
            injector.inject(this)
            injector
        }
        bind<Client>() with singleton { DebugClient() }
        bind<ManagedSQLiteOpenHelper>() with provider {
            DebugSqlHelper(RuntimeEnvironment.application)
        }

        import(debugAuthorizationModule)
        import(debugDashboardModule)
    }

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
    }

    override fun beforeTest(method: Method?) {
    }

    override fun prepareTest(test: Any?) {
    }

    override fun afterTest(method: Method?) {
    }
}

/*
Robolectric EXAMPLE
@RunWith(RobolectricTestRunner::class)
class RegistrationTest {
    @Test
    fun `non valid data should displays error`() {
        val activity = Robolectric.setupActivity(RegistrationActivity::class.java)
        val registerButton = activity.findViewById(R.id.registerButton) as Button
        val phoneInputLayout = activity.findViewById(R.id.phoneInputLayout) as TextInputLayout

        registerButton.performClick()
        assert(phoneInputLayout.error != null)
    }
}
 */
