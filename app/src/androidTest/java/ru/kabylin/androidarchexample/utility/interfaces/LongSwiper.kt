package ru.kabylin.androidarchexample.utility.interfaces

import android.support.test.espresso.UiController

interface LongSwiper {
    enum class Status {
        /**
         * The swipe action completed successfully.
         */
        SUCCESS,
        /**
         * Injecting the event was a complete failure.
         */
        FAILURE
    }

    fun sendSwipe(uiController: UiController,
                  startCoordinates: FloatArray,
                  endCoordinates: List<FloatArray>,
                  precision: FloatArray): Status
}