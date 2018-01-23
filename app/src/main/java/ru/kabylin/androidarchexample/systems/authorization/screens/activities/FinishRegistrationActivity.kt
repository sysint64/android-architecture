package ru.kabylin.androidarchexample.systems.authorization.screens.activities

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.Log
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
import ru.kabylin.androidarchexample.credentials.Credentials
import ru.kabylin.androidarchexample.prefs.StoredUserInfo
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.FinishRegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.screens.views.FinishRegistrationView

class FinishRegistrationActivity : KodeinAppCompatActivity(), FinishRegistrationView, RequestStateListener, ApiValidationErrorListener {
    private val presenter: FinishRegistrationPresenter by with(this).instance()

    private var firstPassword: String = ""
    private var password: String = ""
    private var step = 1

    override fun provideOverridingModule() = Kodein.Module {
        bind<RequestStateListener>() with instance(this@FinishRegistrationActivity)
        bind<ApiValidationErrorListener>() with instance(this@FinishRegistrationActivity)
        bind<KodeinInjector>() with instance(this@FinishRegistrationActivity.injector)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_code)
        progressBar.visibility = View.GONE

        RxPatternLockView.patternComplete(patternLockView)
            .subscribeBy { event -> onCompletePattern(event.pattern) }

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
        Log.d("FRA", "step: $step")

        when (step) {
            0, 1 -> {
                if (pattern.size < 5) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
                    switchToStep(1)
                } else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)
                    firstPassword = patternString
                    switchToStep(2)
                }
            }
            2 -> {
                if (firstPassword != patternString) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
                    switchToStep(1)
                } else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)

                    switchToStep(3)

                    val activatedCode = intent.extras["activatedCode"] as String
                    val secretCode = intent.extras["secretCode"] as String

                    password = patternString
                    presenter.finishRegistration(password, secretCode, activatedCode)
                }
            }
        }
    }

    private fun switchToStep(newStep: Int) {
        step = newStep

        when (step) {
            0 -> {
                patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
            }
            1 -> setNoteText(R.string.pass_code_set_key)
            2 -> {
                setNoteText(R.string.pass_code_repeat_key)
                patternLockView.clearPattern()
            }
        }
    }

    private fun setNoteText(@StringRes text: Int, isError: Boolean = false) {
        val color = if (isError) {
            ContextCompat.getColor(this, R.color.error)
        } else {
            ContextCompat.getColor(this, R.color.white)
        }

        noteText.setText(text)
        noteText.setTextColor(color)
    }

    override fun onStartRequest(requestCode: Int) {
        progressBar.visibility = View.VISIBLE
    }

    override fun onFinishRequest(requestCode: Int) {
        progressBar.visibility = View.GONE
    }

    override fun onApiValidationError(requestCode: Int, errors: ApiValidationError) {
        setNoteText(R.string.something_went_wrong, true)
        switchToStep(0)
    }

    override fun onRegistrationFinished(credentials: Credentials) {
        StoredUserInfo.phone = intent.extras["phone"] as String
        StoredUserInfo.password = password

//        val intent = Intent(this, DashboardActivity::class.java)
//        startActivity(intent)
    }
}
