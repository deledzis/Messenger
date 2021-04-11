package com.deledzis.messenger

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.features.main.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Test

//@RunWith(AndroidJUnit4::class)
//@LargeTest
class MainActivityBehaviourTest {

    /*@get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)*/

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals(
            "com.deledzis.messenger",
            appContext.packageName
        )
    }

    @Test
    fun someTest() {
        val scenario = ActivityScenario.launch<MainActivity>(
            Intent(
                InstrumentationRegistry.getInstrumentation().context,
                MainActivity::class.java
            )
        )
        scenario.onActivity {
            // Type text and then press the button.
            onView(withId(R.id.login_button))
                .check(matches(isDisplayed()))
            onView(withId(R.id.login_button))
                .check(matches(isEnabled()))
            onView(withId(R.id.tie_username))
                .perform(typeText("testtest"), closeSoftKeyboard())
            onView(withId(R.id.tie_password))
                .perform(typeText("testtest"), closeSoftKeyboard())
        }
    }

}