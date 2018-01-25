package ru.kabylin.androidarchexample.systems.authorization.activities

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.common.patternLockViewDoesNotWRONG
import ru.kabylin.androidarchexample.utility.GeneralLongSwipeAction
import ru.kabylin.androidarchexample.utility.interfaces.ManyCoordinatorsProvider
import ru.kabylin.androidarchexample.utility.viewActions.LongSwipe

@RunWith(AndroidJUnit4::class)
class FinishRegistrationActivityTest {
    private var activityTestRule = ActivityTestRule(FinishRegistrationActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun inputPassword() {
        val activity = activityTestRule.launchActivity(Intent())

        val swipeCode = GeneralLongSwipeAction(
            LongSwipe.SLOW,
            GeneralLocation.TOP_LEFT,
            ManyCoordinatorsProvider(
                arrayOf(
                    GeneralLocation.BOTTOM_RIGHT,
                    GeneralLocation.BOTTOM_CENTER,
                    GeneralLocation.TOP_CENTER,
                    GeneralLocation.TOP_RIGHT,
                    GeneralLocation.BOTTOM_LEFT)),
            Press.FINGER)

        onView(withId(R.id.patternLockView))
            .perform(swipeCode)
            .check(matches(patternLockViewDoesNotWRONG()))
    }
}
