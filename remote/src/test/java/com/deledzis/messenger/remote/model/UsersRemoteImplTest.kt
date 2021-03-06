package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.RemoteTestData
import com.deledzis.messenger.remote.UsersRemoteImpl
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
            val usersWithSearch = usersRemoteImpl.getUsers(search = "asdsajdklsad")
            Assertions.assertEquals(usersAll, RemoteTestData.users)
            Assertions.assertNotEquals(usersWithSearch, usersAll)
        }
    }
}