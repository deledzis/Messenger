package com.deledzis.messenger.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
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
class Test13_AddChatDifferentScenariosTest {

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
    fun addChatAndSendMessageTest() {
        // GIVEN
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        UiThreadStatement.runOnUiThread {
            navController.setGraph(R.navigation.nav_graph)
        }

        /**
         * Starting from login screen, entering correct credentials, login succeeds
         **/
        // VERIFY
        onView(withId(R.id.login_button))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_username))
            .perform(typeText("username"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(withId(R.id.tie_password))
            .perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.login_button))
            .perform(click())
        Thread.sleep(10000)

        // VERIFY
        onView(withId(R.id.fragment_chats_root))
            .check(matches(isDisplayed()))
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.chatsFragment)


        // GIVEN
        onView(withId(R.id.ic_add))
            .check(matches(isDisplayed()))
            .perform(click())

        // VERIFY
        Thread.sleep(2000)
        onView(withId(R.id.fragment_add_chat_root))
            .check(matches(isDisplayed()))
        onView(withText(R.string.add_chat_init_title))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_search))
            .perform(typeText("test2"), closeSoftKeyboard())

        // VERIFY
        Thread.sleep(3000)
        onView(withText("dog so low"))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_search))
            .perform(clearText(), typeText("test100500"), closeSoftKeyboard())

        // VERIFY
        Thread.sleep(3000)
        onView(withText(R.string.add_chat_empty_title))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_search))
            .perform(clearText(), typeText("testing"), closeSoftKeyboard())

        // VERIFY
        Thread.sleep(3000)
        onView(withText(R.string.add_chat_empty_title))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.ic_back))
            .check(matches(isEnabled()))
            .perform(click())

        // VERIFY
        Thread.sleep(500)
        onView(withId(R.id.fragment_chats_root))
            .check(matches(isDisplayed()))
        Thread.sleep(2500)

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
        onView(withText(R.string.dialog_btn_exit))
            .perform(click())
        Thread.sleep(500)

        //VERIFY
        onView(withId(R.id.fragment_login_root))
            .check(matches(isDisplayed()))
    }

}