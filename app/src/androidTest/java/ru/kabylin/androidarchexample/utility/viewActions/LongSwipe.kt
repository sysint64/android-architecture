package ru.kabylin.androidarchexample.utility.viewActions

import android.os.SystemClock
import android.support.test.espresso.UiController
import android.support.test.espresso.action.MotionEvents
import android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkElementIndex
import android.util.Log
import ru.kabylin.androidarchexample.systems.authorization.presenters.LoginPresenter.Companion.TAG
import ru.kabylin.androidarchexample.utility.interfaces.LongSwiper

enum class LongSwipe : LongSwiper {
    FAST {
        override fun sendSwipe(uiController: UiController,
                               startCoordinates: FloatArray,
                               endCoordinates: List<FloatArray>,
                               precision: FloatArray): LongSwiper.Status
        {
            return Companion.sendSwipe(
                uiController,
                startCoordinates,
                endCoordinates,
                precision,
                SWIPE_FAST_DURATION_MS)
        }
    },

    SLOW {
        override fun sendSwipe(uiController: UiController,
                               startCoordinates: FloatArray,
                               endCoordinates: List<FloatArray>,
                               precision: FloatArray): LongSwiper.Status
        {
            return Companion.sendSwipe(
                uiController,
                startCoordinates,
                endCoordinates,
                precision,
                SWIPE_SLOW_DURATION_MS)
        }
    };

    private companion object {
        private val SWIPE_FAST_DURATION_MS = 800
        private val SWIPE_SLOW_DURATION_MS = 3000

        private val SWIPE_EVENT_COUNT = 10

        private fun interpolate(start: FloatArray, end: FloatArray, steps: Int): Array<FloatArray> {
            checkElementIndex(1, start.size)
            checkElementIndex(1, end.size)

            val res = Array(steps) { FloatArray(2) }

            for (i in 1 until steps + 1) {
                res[i - 1][0] = start[0] + (end[0] - start[0]) * i / (steps + 2f)
                res[i - 1][1] = start[1] + (end[1] - start[1]) * i / (steps + 2f)
            }

            return res
        }

        fun sendSwipe(uiController: UiController,
                      startCoordinates: FloatArray,
                      endCoordinates: List<FloatArray>,
                      precision: FloatArray,
                      duration: Int): LongSwiper.Status
        {
            val downEvent = MotionEvents.sendDown(uiController, startCoordinates, precision).down
            var from = startCoordinates
            try {
                for (to in endCoordinates) {
                    val steps = interpolate(from, to, SWIPE_EVENT_COUNT)
                    val delayBetweenMovements = duration / steps.size

                    for (i in steps.indices) {
                        if (!MotionEvents.sendMovement(uiController, downEvent, steps[i])) {
                            Log.e(TAG, "Injection of move event as part of the swipe failed. Sending cancel event.")
                            MotionEvents.sendCancel(uiController, downEvent)
                            return LongSwiper.Status.FAILURE
                        }

                        val desiredTime = downEvent.downTime + delayBetweenMovements * i
                        val timeUntilDesired = desiredTime - SystemClock.uptimeMillis()
                        if (timeUntilDesired > 10) {
                            uiController.loopMainThreadForAtLeast(timeUntilDesired)
                        }
                    }
                    from = to
                }

                if (!MotionEvents.sendUp(uiController, downEvent, endCoordinates.last())) {
                    Log.e(TAG, "Injection of up event as part of the swipe failed. Sending cancel event.")
                    MotionEvents.sendCancel(uiController, downEvent)
                    return LongSwiper.Status.FAILURE
                }
            } finally {
                downEvent.recycle()
            }
            return LongSwiper.Status.SUCCESS
        }
    }
}