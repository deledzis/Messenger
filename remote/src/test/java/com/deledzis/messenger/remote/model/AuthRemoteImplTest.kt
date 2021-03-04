package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.AuthRemoteImpl
import com.deledzis.messenger.remote.TestData
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthRemoteImplTest {

    var apiService: ApiService = MockNetworkModule().buildService()

    var authRemoteImpl: AuthRemoteImpl = AuthRemoteImpl(apiService)

    @Test
    fun loginTest() = runBlocking {

        val authUser = authRemoteImpl.login(username = "Igor", password = "c123456789")
        assertEquals(authUser, TestData.auth)
    }

    @Test
    fun registerTest() = runBlocking {

        val authUser = authRemoteImpl.register(username = "Igor", nickname = "Igorek", password = "c123456789")
        assertEquals(authUser.username, "Igor")
    }

    @Test
    fun updateUserDataTest() = runBlocking {

        val authUser = authRemoteImpl.updateUserData(username = "Igor", nickname = "Igorek", password = "c123456789", newPassword = null)
        assertEquals(authUser.username, "Igor")
    }
}