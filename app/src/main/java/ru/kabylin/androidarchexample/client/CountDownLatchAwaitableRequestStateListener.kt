package ru.kabylin.androidarchexample.client

import android.util.SparseArray
import ru.kabylin.androidarchexample.common.Service
import java.util.concurrent.CountDownLatch

class CountDownLatchAwaitableRequestStateListener : AwaitableRequestStateListener {
    private var latch : CountDownLatch? = null
    private val latches = SparseArray<CountDownLatch>()

    override fun awaitRequest(requestCode: Int) {
        if (requestCode == Service.REQUEST_DEFAULT) {
            latch?.await()
            latch = null
        } else {
            latches[requestCode]?.await()
            latches.remove(requestCode)
        }
    }

    override fun onStartRequest(requestCode: Int) {
        if (requestCode == Service.REQUEST_DEFAULT) {
            latch = CountDownLatch(1)
        } else {
            latches.append(requestCode, CountDownLatch(1))
        }
    }

    override fun onFinishRequest(requestCode: Int) {
        if (requestCode == Service.REQUEST_DEFAULT) {
            latch?.countDown()
        } else {
            latches[requestCode]?.countDown()
        }
    }
}
