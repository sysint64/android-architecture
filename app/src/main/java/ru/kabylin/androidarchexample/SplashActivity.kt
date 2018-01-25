package ru.kabylin.androidarchexample

import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.instance
import ru.kabylin.androidarchexample.client.credentials.CredentialsProvider
import ru.kabylin.androidarchexample.prefs.StoredUserInfo
import ru.kabylin.androidarchexample.systems.authorization.activities.LoginActivity
import ru.kabylin.androidarchexample.systems.authorization.activities.RegistrationActivity

class SplashActivity : KodeinAppCompatActivity() {
    private val credentialsProvider: CredentialsProvider by instance("api")
    private val presenter = SplashPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
        presenter.doRequest()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    fun toNextScreen() {
        val credentials = credentialsProvider.provide()

        when {
            credentials == null -> {
                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)
            }
//            !credentials.hasExpired() -> {
//                val intent = Intent(this, DashboardActivity::class.java)
//                startActivity(intent)
//            }
            else -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("phone", StoredUserInfo.phone)
                startActivity(intent)
            }
        }
    }
}
