package com.deledzis.messenger.presentation.features.login

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
//import androidx.test.ext.junit.runners.AndroidJUnit4
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.features.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
//@LargeTest
//class LoginFragmentTest {
//
//    private lateinit var stringToBetyped: String
////    private lateinit var scenario: FragmentScenario<LoginFragment>
//
////    @get:Rule
////    var activityRule: ActivityScenarioRule<MainActivity>
////            = ActivityScenarioRule(MainActivity::class.java)
//
//    @Before
//    fun init() {
//        // Specify a valid string.
//        stringToBetyped = "Espresso"
////        activityRule.scenario.activi
////            .getSupportFragmentManager().beginTransaction();
//    }
//
//    @Test
//    fun changeText_sameActivity() {
//        val scenario = launchFragmentInContainer<LoginFragment>()
//        // Type text and then press the button.
//        onView(withId(R.id.til_username))
//            .perform(typeText(stringToBetyped), closeSoftKeyboard())
//        onView(withId(R.id.til_username))
//            .check(matches(withText(stringToBetyped)))
//    }
//}