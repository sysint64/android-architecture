package ru.kabylin.androidarchexample.common

import android.view.View
import com.andrognito.patternlockview.PatternLockView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun patternLockViewDoesNotWRONG(): Matcher<View> {
    return PatternLockViewMatcher as Matcher<View>
}

object PatternLockViewMatcher : TypeSafeMatcher<PatternLockView>() {
    override fun matchesSafely(item: PatternLockView?): Boolean {
        item ?: return false
        return (item.patternViewMode != PatternLockView.PatternViewMode.WRONG)
    }

    override fun describeTo(description: Description?) {
        description?.appendText("PatternLockView does not WRONG")
    }
}
