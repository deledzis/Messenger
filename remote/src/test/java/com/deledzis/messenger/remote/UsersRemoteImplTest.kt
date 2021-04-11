package com.deledzis.messenger.remote

import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class UsersRemoteImplTest {
    var apiService: ApiService = MockNetworkModule().buildService()

    var usersRemoteImpl: UsersRemoteImpl = UsersRemoteImpl(apiService)

    @Test
    fun getUserTest() {
        assertDoesNotThrow {
            runBlocking {
                val user = usersRemoteImpl.getUser(id = RemoteTestData.user.id!!)
                Assertions.assertEquals(user, RemoteTestData.user)
            }
        }
    }

    @Test
    fun getUsersTest() {
        runBlocking {
            val usersAll = usersRemoteImpl.getUsers(search = "")
            Assertions.assertEquals(usersAll, RemoteTestData.users)
        }
    }
}