package com.example.storyapp.view.welcome

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.storyapp.utils.EspressoIdlingResource
import com.example.storyapp.R
import com.example.storyapp.view.WelcomeActivity
import com.example.storyapp.view.login.LoginActivity
import com.example.storyapp.view.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WelcomeActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(WelcomeActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginThenLogout() {
        Intents.init()
        onView(withId(R.id.loginButton)).perform(click())
        intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.ed_login_email))
            .perform(typeText("sayuti@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .perform(typeText("01123lima8"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText("Lanjut"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withText("Lanjut"))
            .inRoot(isDialog())
            .perform(click())
        intended(hasComponent(MainActivity::class.java.name))
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onView(withText(R.string.logout)).perform(click())
    }
}