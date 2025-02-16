package com.muliamaulana.github

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.UiDevice
import com.muliamaulana.github.features.home.MainActivity
import com.muliamaulana.github.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Created by muliamaulana on 16/02/25.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        val uiDevice =
            UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation())
        uiDevice.executeShellCommand("settings put global window_animation_scale 0")
        uiDevice.executeShellCommand("settings put global transition_animation_scale 0")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 0")
        Intents.init()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        Intents.release()
    }

    @Test
    fun step1showListUser() {
        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))
    }

    @Test
    fun step2ScrollList() {
        onView(withId(R.id.rv_user)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5)
        )
        onView(withId(R.id.rv_user)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        pressBack()
    }

    @Test
    fun step3SearchUser() {
        onView(withId(R.id.search_view)).apply {
            check(matches(isDisplayed()))
            perform(click())
        }

        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(typeText("muliamaulana"), closeSoftKeyboard())

        onView(withId(R.id.rv_search)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_search)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0)
        )

        onView(withId(R.id.rv_search)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        pressBack()

    }

}