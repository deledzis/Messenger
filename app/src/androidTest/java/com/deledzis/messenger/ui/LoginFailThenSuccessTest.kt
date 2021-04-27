package com.deledzis.messenger.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.deledzis.messenger.R
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.presentation.features.main.MainActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginFailThenSuccessTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java, true)

    @Inject
    lateinit var userData: UserData

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val component = DaggerTestAppComponent.builder()
            .testCacheModule(TestCacheModule())
            .testNetworkModule(TestNetworkModule())
            .testRepositoriesModule(TestRepositoriesModule())
            .utilsModule(UtilsModule())
            .testAppModule(TestAppModule(context))
            .build()
        component.into(this)
        userData.saveAuthUser(null)
    }

    @After
    fun tearDown() {
        userData.saveAuthUser(null)
    }

    @Test
    fun loginFailThenSuccessTest() {
        // GIVEN
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        UiThreadStatement.runOnUiThread {
            navController.setGraph(R.navigation.nav_graph)
        }

        /**
         * Starting from login screen, entering wrong credentials, login fails
         * then entering correct credentials, login succeeds
         **/
        // VERIFY
        onView(withId(R.id.login_button))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_username))
            .perform(typeText("username"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(withId(R.id.tie_password))
            .perform(typeText("password_wrong"), closeSoftKeyboard())
        onView(withId(R.id.login_button))
            .perform(click())
        Thread.sleep(7500)

        // VERIFY
        onView(withId(R.id.fragment_login_root))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_username))
            .perform(clearText())
            .perform(typeText("username"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(withId(R.id.tie_password))
            .perform(clearText())
            .perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.login_button))
            .perform(click())
        Thread.sleep(7500)

        /**
         * Chats screen appears, move to settings, do logout, login screen appears then
         **/
        // VERIFY
        onView(withId(R.id.fragment_chats_root))
            .check(matches(isDisplayed()))
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.chatsFragment)

        // GIVEN
        onView(withId(R.id.ic_settings))
            .perform(click())

        // VERIFY
        Thread.sleep(1000)
        onView(withId(R.id.fragment_settings_root))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.btn_logout))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
            .perform(click())
        Thread.sleep(500)
        onView(ViewMatchers.withText("Выйти"))
            .perform(click())
        Thread.sleep(500)

        //VERIFY
        onView(withId(R.id.fragment_login_root))
            .check(matches(isDisplayed()))

    }

}