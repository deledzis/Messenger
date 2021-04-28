package com.deledzis.messenger.cache.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UsersLastCacheTimeTest {
    companion object {
        private const val PREF_APP_PACKAGE_NAME_TEST = "com.deledzis.messenger.preferencestest"
        private const val testLong : Long = 115479321L
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
        val usersLastCacheTime = UsersLastCacheTime(sharedPreferences)
        usersLastCacheTime.lastCacheTime = testLong
        Assert.assertEquals(testLong, usersLastCacheTime.lastCacheTime)
    }

    @Test
    fun removeTest() {
        val usersLastCacheTime = UsersLastCacheTime(sharedPreferences)
        usersLastCacheTime.lastCacheTime = testLong
        usersLastCacheTime.lastCacheTime = null
        Assert.assertEquals(0L, usersLastCacheTime.lastCacheTime)
    }
}