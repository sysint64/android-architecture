package ru.kabylin.androidarchexample.common

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import org.robolectric.RuntimeEnvironment

open class BaseTest : KodeinAware {
    override val kodein: Kodein
        get() = (RuntimeEnvironment.application as KodeinAware).kodein
}
