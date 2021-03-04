package com.deledzis.messenger.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith
class AuthRemoteImplTest {

    var apiService: ApiService = MockNetworkModule().buildService()

    var authRemoteImpl: AuthRemoteImpl = AuthRemoteImpl(apiService)

    @Test
    fun loginTest() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val authUser = authRemoteImpl.login(username = "Igor", password = "c123456789")
                Assertions.assertEquals(authUser.username, "Igor")
            }
        }
    }
}