package ru.kabylin.androidarchexample

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.github.salomonbrys.kodein.*

class MainApplication : Application(), KodeinAware, DataStoreAware {
    override val kodein = dependencies(this)
    override val dataStore: DataStore = kodein.instance()

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
    }
}
