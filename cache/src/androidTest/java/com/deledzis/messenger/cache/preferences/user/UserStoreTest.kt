package com.deledzis.messenger.cache.preferences.user

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.UsersLastCacheTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserStoreTest {
    companion object {
        private const val PREF_APP_PACKAGE_NAME_TEST = "com.deledzis.messenger.preferencestest"
        private const val testString : String = "qwert12347"
    }

    lateinit var sharedPreferences: SharedPreferences
    lateinit var context: Context

    @Before
    fun Setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        sharedPreferences = context.getSharedPreferences(PREF_APP_PACKAGE_NAME_TEST, Context.MODE_PRIVATE)
    }

    @Test
    fun setGetTest() {
        val userStore = UserStore(sharedPreferences)
        userStore.userData = testString
        Assert.assertEquals(testString, userStore.userData)
    }

    @Test
    fun removeTest() {
        val userStore = UserStore(sharedPreferences)
        userStore.userData = testString
        userStore.userData = null
        Assert.assertEquals("", userStore.userData)
    }
}