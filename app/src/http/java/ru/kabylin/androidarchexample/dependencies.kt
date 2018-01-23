package ru.kabylin.androidarchexample

import android.content.Context
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.lazy
import com.github.salomonbrys.kodein.singleton
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.client.HttpClient
import ru.kabylin.androidarchexample.systems.authorization.authorizationModule

fun dependencies(context: Context): Kodein {
    val kodein by Kodein.lazy {
        bind<Client>() with singleton { HttpClient() }

        import(authorizationModule)
    }

    return kodein
}
