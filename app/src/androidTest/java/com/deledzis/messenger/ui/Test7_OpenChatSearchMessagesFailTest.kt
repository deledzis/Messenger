package com.deledzis.messenger.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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
import com.deledzis.messenger.presentation.features.chats.ChatsAdapter
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
class Test7_OpenChatSearchMessagesFailTest {

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
    }

    @After
    fun tearDown() {
        userData.saveAuthUser(null)
    }

    @Test
    fun openChatSearchMessagesFailTest() {
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

        /**
         * From the chats screen open first chat, go to search screen
         **/
        // GIVEN
        onView(withId(R.id.rv_chats))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ChatsAdapter.ViewHolder>(
                    0,
                    click()
                )
            )

        // VERIFY
        Thread.sleep(5000)
        onView(withId(R.id.fragment_chat_root))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.ic_search))
            .check(matches(isEnabled()))
            .perform(click())

        /**
         * From the search screen start searching for "test" messages,
         * then return back to chats screen
         **/
        // VERIFY
        Thread.sleep(500)
        onView(withId(R.id.fragment_search_root))
            .check(matches(isDisplayed()))
        onView(withText("Начните вводить запрос"))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.tie_search))
            .perform(typeText("some_long_text"), closeSoftKeyboard())

        // VERIFY
        Thread.sleep(3000)
        onView(withText("Ничего не найдено \uD83D\uDE14"))
            .check(matches(isDisplayed()))

        // GIVEN
        onView(withId(R.id.ic_back))
            .check(matches(isEnabled()))
            .perform(click())

        // VERIFY
        Thread.sleep(500)
        onView(withId(R.id.fragment_chat_root))
            .check(matches(isDisplayed()))
        Thread.sleep(2500)

        // GIVEN
        onView(withId(R.id.ic_back))
            .check(matches(isEnabled()))
            .perform(click())

        /**
         * From the chats screen go to settings screen and logout
         **/
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
        onView(withText("Выйти"))
            .perform(click())
        Thread.sleep(500)

        //VERIFY
        onView(withId(R.id.fragment_login_root))
            .check(matches(isDisplayed()))

    }

}