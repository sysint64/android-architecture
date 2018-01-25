package ru.kabylin.androidarchexample.systems.authorization

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.androidActivityScope
import ru.kabylin.androidarchexample.client.credentials.CredentialsProvider
import ru.kabylin.androidarchexample.client.credentials.AuthTokentCredentialsProvider
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.FinishRegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.LoginPresenter
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.RegistrationPresenter
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.VerifySmsPresenter
import ru.kabylin.androidarchexample.systems.authorization.services.AuthService
import ru.kabylin.androidarchexample.systems.authorization.services.HttpAuthService
import ru.kabylin.androidarchexample.systems.authorization.services.HttpRegistrationService
import ru.kabylin.androidarchexample.systems.authorization.services.RegistrationService

val authorizationModule = Kodein.Module {
    bind<CredentialsProvider>("api") with singleton {
        AuthTokentCredentialsProvider()
    }

    // Login
    bind<LoginPresenter>() with scopedSingleton(androidActivityScope) {
        LoginPresenter(
            service = kodein.instance()
        )
    }

    bind<AuthService>() with provider {
        HttpAuthService(
            client = kodein.instance(),
            injector = kodein.instance()
        )
    }

    // Registration
    bind<RegistrationPresenter>() with scopedSingleton(androidActivityScope) {
        RegistrationPresenter(kodein.instance())
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
        HttpRegistrationService(
            client = kodein.instance(),
            injector = kodein.instance()
        )
    }
}
