package ru.kabylin.androidarchexample.common

import android.support.design.widget.TextInputLayout
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun textViewDoesHaveErrors(): Matcher<View> {
    return TextInputLayoutMatcher as Matcher<View>
}

object TextInputLayoutMatcher : TypeSafeMatcher<TextInputLayout>() {
    override fun matchesSafely(item: TextInputLayout?): Boolean {
        return item?.isErrorEnabled ?: false
    }

    override fun describeTo(description: Description?) {
        description?.appendText("TextInputLayout not have error.")
    }
}
