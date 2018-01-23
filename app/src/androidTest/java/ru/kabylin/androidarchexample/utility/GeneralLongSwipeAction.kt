package ru.kabylin.androidarchexample.utility

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import android.support.test.espresso.util.HumanReadables
import android.view.View
import android.view.ViewConfiguration
import org.hamcrest.Matcher
import ru.kabylin.androidarchexample.utility.interfaces.LongSwiper
import ru.kabylin.androidarchexample.utility.interfaces.ManyCoordinatorsProvider

class GeneralLongSwipeAction(
    private val swiper: LongSwiper,
    private val startCoordinatesProvider: CoordinatesProvider,
    private val endCoordinatesProvider: ManyCoordinatorsProvider,
    private val precisionDescriber: PrecisionDescriber
) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isDisplayingAtLeast(VIEW_DISPLAY_PERCENTAGE)
    }

    override fun perform(uiController: UiController, view: View) {
        val startCoordinates = startCoordinatesProvider.calculateCoordinates(view)
        val endCoordinates = endCoordinatesProvider.calculateCoordinates(view)
        val precision = precisionDescriber.describePrecision()
        var status: LongSwiper.Status = LongSwiper.Status.FAILURE
        var tries = 0

        while (tries < MAX_TRIES && status != LongSwiper.Status.SUCCESS) {
            try {
                status = swiper.sendSwipe(uiController, startCoordinates, endCoordinates, precision)
            } catch (re: RuntimeException) {
                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(re)
                    .build()
            }

            val duration = ViewConfiguration.getPressedStateDuration()

            if (duration > 0) {
                uiController.loopMainThreadForAtLeast(duration.toLong())
            }
            tries++
        }

        if (status == LongSwiper.Status.FAILURE) {
            throw PerformException.Builder()
                .withActionDescription(description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(RuntimeException("""
                Couldn't swipe.
                Swiper: $swiper start coordinate provider:
                $startCoordinatesProvider precision
                describer: $precisionDescriber. Tried $MAX_TRIES times.""".trimIndent()))
                .build()
        }
    }

    override fun getDescription(): String {
        return swiper.toString().toLowerCase() + " swipe"
    }

    companion object {

        /** Maximum number of times to attempt sending a swipe action.  */
        private val MAX_TRIES = 3

        /** The minimum amount of a view that must be displayed in order to swipe across it.  */
        private val VIEW_DISPLAY_PERCENTAGE = 90
    }
}