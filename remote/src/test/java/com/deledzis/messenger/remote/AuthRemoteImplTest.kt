package com.deledzis.messenger.remote

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith
class AuthRemoteImplTest {

    var apiService: ApiService = MockNetworkModule().buildClient()

//    lateinit var apiServiceMock: ApiService

    var authRemoteImpl: AuthRemoteImpl = AuthRemoteImpl(apiService)

//    @BeforeEach
//    fun setUp() {
//        apiServiceMock = Mockito.mock(ApiService::class.java)
//        authRemoteImpl = AuthRemoteImpl(apiServiceMock)
//    }


    @Test
    fun loginTest() = runBlocking {
//        Mockito.`when`(
//            apiService.login(
//                AuthUserRequest(
//                    username = "Igor",
//                    password = "c123456789"
//                )
//            )
//        ).thenReturn(
//            AuthEntity(
//                id = 1,
//                username = "Igor",
//                nickname = "igorek",
//                accessToken = "123",
//                errorCode = null,
//                message = null
//            )
//        )

        val authUser = authRemoteImpl.login(username = "Igor", password = "c123456789")
        assertEquals(authUser.username, "Igor")
    }
}