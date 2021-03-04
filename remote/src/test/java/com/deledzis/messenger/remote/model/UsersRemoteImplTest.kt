package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.TestData
import com.deledzis.messenger.remote.UsersRemoteImpl
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class UsersRemoteImplTest {
    var apiService: ApiService = MockNetworkModule().buildService()

    var usersRemoteImpl: UsersRemoteImpl = UsersRemoteImpl(apiService)

    @Test
    fun getUserTest() {
        assertDoesNotThrow {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val user = usersRemoteImpl.getUser(id = TestData.user.id!!)
                    Assertions.assertEquals(user, TestData.user)
                }
            }
        }
    }

    @Test
    fun getUsersTest() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val usersAll = usersRemoteImpl.getUsers(search = "")
                val usersWithSearch = usersRemoteImpl.getUsers(search = "asdsajdklsad")
                Assertions.assertEquals(usersAll, TestData.users)
                Assertions.assertNotEquals(usersWithSearch, usersAll)
            }
        }
    }
}