package ru.kabylin.androidarchexample.systems.authorization.screens.activities

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.salomonbrys.kodein.instance
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.kabylin.androidarchexample.R
import ru.kabylin.androidarchexample.client.Client
import ru.kabylin.androidarchexample.systems.authorization.screens.presenters.RegistrationPresenter
import ru.kabylin.androidarchexample.common.textViewDoesHaveErrors

@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {
    private var activityTestRule = ActivityTestRule(RegistrationActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkPhoneInputIsDisplayed() {
        activityTestRule.launchActivity(Intent())
        onView(withId(R.id.phoneInput)).check(matches(isDisplayed()))
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }

    @Test
    fun inputIncorrectPhone() {
        val activity = activityTestRule.launchActivity(Intent())
        onView(withId(R.id.phoneInput))
            .perform(typeText("960650"))
        onView(withId(R.id.registerButton))
            .perform(click())

        onView(withId(R.id.phoneInputLayout))
            .check(matches(textViewDoesHaveErrors()))
    }

    @Test
    fun inputRightPhone() {
        val activity = activityTestRule.launchActivity(Intent())
        val client by activity.injector.instance<Client>()

        onView(withId(R.id.phoneInput))
            .perform(typeText("9606504030"))
        onView(withId(R.id.registerButton))
            .perform(click())

        client.awaitRequest(RegistrationPresenter.REQUEST_SERVICE_REQUEST_REGISTRATION)

        onView(withId(R.id.smsCodeInputLayout))
            .check(matches(isDisplayed()))
    }
}
