package ru.kabylin.androidarchexample.systems.authorization.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.andrognito.rxpatternlockview.RxPatternLockView
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_pass_code.*
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.client.RequestStateListener
import ru.kabylin.androidarchexample.client.api.ApiValidationErrorListener
import ru.kabylin.androidarchexample.client.api.ApiValidationError
import ru.kabylin.androidarchexample.systems.authorization.presenters.LoginPresenter
import ru.kabylin.androidarchexample.systems.authorization.views.LoginView

class LoginActivity : KodeinAppCompatActivity(), LoginView, RequestStateListener, ApiValidationErrorListener {
    private val presenter: LoginPresenter by with(this).instance()
    private lateinit var phone: String

    override fun provideOverridingModule() = Kodein.Module {
        bind<RequestStateListener>() with instance(this@LoginActivity)
        bind<ApiValidationErrorListener>() with instance(this@LoginActivity)
        bind<KodeinInjector>() with instance(this@LoginActivity.injector)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_code)

        noteText.setText(R.string.pass_code_enter_key)
        restorePasswordButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        RxPatternLockView.patternComplete(patternLockView)
            .subscribeBy { event -> onCompletePattern(event.pattern) }

        phone = intent.extras["phone"] as String
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    private fun onCompletePattern(pattern: MutableList<PatternLockView.Dot>?) {
        if (pattern == null) {
            patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
            return
        }

        val patternString = PatternLockUtils.patternToString(patternLockView, pattern)
        presenter.login(phone, patternString)
    }

    override fun onSignedIn() {
        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)
//        val intent = Intent(this, DashboardActivity::class.java)
//        startActivity(intent)
    }

    override fun onStartRequest(requestCode: Int) {
        progressBar.visibility = View.VISIBLE
        patternLockView.correctStateColor = ContextCompat.getColor(this, R.color.gray)
    }

    override fun onFinishRequest(requestCode: Int) {
        progressBar.visibility = View.GONE
        patternLockView.correctStateColor = ContextCompat.getColor(this, R.color.green)
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
    }
}
