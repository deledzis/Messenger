package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.AuthRemoteImpl
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthRemoteImplTest {

    var apiService: ApiService = MockNetworkModule().buildService()

    var authRemoteImpl: AuthRemoteImpl = AuthRemoteImpl(apiService)

    companion object {
        const val testUsername: String = "Igor"
        const val testNickname: String = "Igorek"
        const val testPassword: String = "c123456789"
    }

    @Test
    fun loginTest() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val authUser =
                    authRemoteImpl.login(username = testUsername, password = testPassword)

                assertEquals(testUsername, authUser.username)
                assertEquals(testNickname, authUser.nickname)
                Assertions.assertTrue(authUser.accessToken.isNotEmpty())
            }
        }
    }

    @Test
    fun registerTest() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val authUser = authRemoteImpl.register(
                    username = testUsername,
                    nickname = testNickname,
                    password = testPassword
                )

                assertEquals(testUsername, authUser.username)
                assertEquals(testNickname, authUser.nickname)
                Assertions.assertTrue(authUser.accessToken.isNotEmpty())
            }
        }
    }

    @Test
    fun updateUserDataTest() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val authUser = authRemoteImpl.updateUserData(
                    username = testUsername,
                    nickname = testNickname,
                    password = testPassword,
                    newPassword = null
                )

                assertEquals(testUsername, authUser.username)
                assertEquals(testNickname, authUser.nickname)
                Assertions.assertTrue(authUser.accessToken.isNotEmpty())
            }
        }
    }
}