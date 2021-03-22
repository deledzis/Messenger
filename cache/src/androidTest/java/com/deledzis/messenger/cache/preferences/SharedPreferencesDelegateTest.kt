package com.deledzis.messenger.cache.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserStore
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SharedPreferencesDelegateTest {
    companion object {
        private const val PREF_APP_PACKAGE_NAME_TEST = "com.deledzis.messenger.preferencestest"
        private const val testString = "qwer123"
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
        var prefString: String? by PreferencesDelegate(
            sharedPreferences,
            PREF_APP_PACKAGE_NAME_TEST,
            ""
        )

        prefString = testString
        assertEquals(prefString, testString)
    }

    @Test
    fun removeTest() {
        var prefString: String? by PreferencesDelegate(
            sharedPreferences,
            PREF_APP_PACKAGE_NAME_TEST,
            ""
        )

        prefString = null
        assertEquals(prefString, "")
    }
}