package com.deledzis.messenger.cache.preferences.user

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthenticatedUserTest {

    companion object {
        private const val PREF_APP_PACKAGE_NAME_TEST = "com.deledzis.messenger.preferencestest"
        private val testAuthUser = AuthenticatedUser(1, "Vitaliy", "Vitya", "123789")
    }

    lateinit var sharedPreferences: SharedPreferences
    lateinit var context: Context
    lateinit var userStore: UserStore

    @Before
    fun Setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        sharedPreferences = context.getSharedPreferences(PREF_APP_PACKAGE_NAME_TEST, Context.MODE_PRIVATE)
        userStore = UserStore(sharedPreferences)
    }

    @Test
    fun saveRestoreTest() {
        testAuthUser.save(userStore)
        val authedUser = AuthenticatedUser.restore(userStore)
        Assert.assertEquals(authedUser?.id, testAuthUser.id)
        Assert.assertEquals(authedUser?.username, testAuthUser.username)
        Assert.assertEquals(authedUser?.nickname, testAuthUser.nickname)
        Assert.assertEquals(authedUser?.accessToken, testAuthUser.accessToken)
    }

    @Test
    fun removeTest() {
        AuthenticatedUser.clear(userStore)
        val authedUser = AuthenticatedUser.restore(userStore)
        Assert.assertEquals(null, authedUser)
    }
}