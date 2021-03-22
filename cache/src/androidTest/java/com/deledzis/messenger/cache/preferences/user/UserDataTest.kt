package com.deledzis.messenger.cache.preferences.user

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.mapper.UserMapper
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.google.firebase.analytics.FirebaseAnalytics
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDataTest {
    companion object {
        private const val PREF_APP_PACKAGE_NAME_TEST = "com.deledzis.messenger.preferencestest"
        private val testAuth = Auth(1, "Vitaliy", "Vitya", "123789")
    }

    lateinit var sharedPreferences: SharedPreferences
    lateinit var context: Context
    lateinit var userStore: UserStore
    lateinit var userData: UserData
    lateinit var analytics: FirebaseAnalytics

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        sharedPreferences = context.getSharedPreferences(PREF_APP_PACKAGE_NAME_TEST, Context.MODE_PRIVATE)
        analytics = FirebaseAnalytics.getInstance(context)
        userStore = UserStore(sharedPreferences)
        userData = UserData(userStore, UserMapper(), analytics)
    }

    @Test
    fun saveAuthUser() {
        userData.saveAuthUser(testAuth)
        Assert.assertEquals(userData.user?.id, testAuth.id)
        Assert.assertEquals(userData.user?.username, testAuth.username)
        Assert.assertEquals(userData.user?.nickname, testAuth.nickname)
        Assert.assertEquals(userData.user?.accessToken, testAuth.accessToken)
    }

    @Test
    fun getAuthUserTest() {
        val authedUser = userData.getAuthUser()
        Assert.assertEquals(userData.user?.id, authedUser?.id)
        Assert.assertEquals(userData.user?.username, authedUser?.username)
        Assert.assertEquals(userData.user?.nickname, authedUser?.nickname)
        Assert.assertEquals(userData.user?.accessToken, authedUser?.accessToken)
    }

    @Test
    fun setNullTest() {
        userData.saveAuthUser(null)
        Assert.assertEquals(userData.user, null)
    }
}