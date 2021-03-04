package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.MessagesRemoteImpl
import com.deledzis.messenger.remote.TestData
import com.deledzis.messenger.remote.UsersRemoteImpl
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UsersRemoteImplTest {
    var apiService: ApiService = MockNetworkModule().buildService()

    var usersRemoteImpl: UsersRemoteImpl = UsersRemoteImpl(apiService)

    @Test
    fun getUserTest() = runBlocking {
        val user = usersRemoteImpl.getUser(TestData.user.id ?: 1)
        Assertions.assertEquals(user, TestData.user)
    }

    @Test
    fun getUsersTest() = runBlocking {
        val users = usersRemoteImpl.getUsers("")
        Assertions.assertEquals(users, TestData.users)
    }
}