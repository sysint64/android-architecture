package ru.kabylin.androidarchexample.utility.interfaces

import android.support.test.espresso.action.CoordinatesProvider
import android.view.View

class ManyCoordinatorsProvider(val coordinatesProvideres: Array<CoordinatesProvider>) {
    fun calculateCoordinates(view: View): List<FloatArray> {
        val result = ArrayList<FloatArray>()
        coordinatesProvideres.forEach {
            result.add(it.calculateCoordinates(view))
        }
        return result
    }
}
