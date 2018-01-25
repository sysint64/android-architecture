package ru.kabylin.androidarchexample.systems.authorization

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.androidActivityScope
import ru.kabylin.androidarchexample.client.credentials.CredentialsProvider
import ru.kabylin.androidarchexample.client.credentials.DebugTokenCredentialsProvider
import ru.kabylin.androidarchexample.systems.authorization.activities.DebugRegistrationActivityDelegate
import ru.kabylin.androidarchexample.systems.authorization.activities.RegistrationActivity
import ru.kabylin.androidarchexample.systems.authorization.presenters.FinishRegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.presenters.LoginPresenter
import ru.kabylin.androidarchexample.systems.authorization.presenters.RegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.presenters.VerifySmsPresenter
import ru.kabylin.androidarchexample.systems.authorization.services.DebugAuthService
import ru.kabylin.androidarchexample.systems.authorization.services.DebugRegistrationService
import ru.kabylin.androidarchexample.systems.authorization.services.AuthService
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService

val debugAuthorizationModule = Kodein.Module {
    bind<CredentialsProvider>("api") with singleton {
        DebugTokenCredentialsProvider()
    }

    // Login
    bind<LoginPresenter>() with scopedSingleton(androidActivityScope) {
        LoginPresenter(
            service = kodein.instance()
        )
    }

    bind<AuthService>() with provider {
        DebugAuthService(
            client = kodein.instance(),
            database = kodein.instance(),
            injector = kodein.instance()
        )
    }

    // Registration
    bind<RegistrationPresenter>() with scopedSingleton(androidActivityScope) {
        RegistrationPresenter(kodein.instance())
    }

    bind<RegistrationActivity.Delegate>() with scopedSingleton(androidActivityScope) {
        DebugRegistrationActivityDelegate()
    }

    bind<VerifySmsPresenter>() with scopedSingleton(androidActivityScope) {
        VerifySmsPresenter(kodein.instance())
    }

    bind<FinishRegistrationPresenter>() with scopedSingleton(androidActivityScope) {
        FinishRegistrationPresenter(
            registrationService = kodein.instance()
        )
    }

    bind<RegistrationService>() with provider {
        DebugRegistrationService(
            client = kodein.instance(),
            database = kodein.instance(),
            injector = kodein.instance()
        )
    }
}
