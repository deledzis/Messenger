package com.deledzis.messenger.data.repository.auth

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.model.response.auth.UpdateUserDataResponse
import com.deledzis.messenger.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class AuthRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
//        val component =
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun login() {
        runBlockingTest {
            val result = repository.login(
                username = "username",
                password = "password"
            )
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertEquals(data.username, "username")
            Assertions.assertEquals(data.nickname, "nickname")
        }
    }

    @Test
    fun register() {
        runBlockingTest {
            repository = Mockito.mock(AuthRepository::class.java)
            Mockito.`when`(
                repository.register(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.anyString()
                )
            ).then {
                return@then Response.Success(
                    RegisterResponse(
                        Auth(
                            id = 0,
                            username = "username",
                            nickname = "nickname",
                            accessToken = "accessToken"
                        )
                    )
                )
            }
            val result = repository.register(
                username = "username",
                nickname = "nickname",
                password = "password"
            )
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertEquals(data.username, "username")
            Assertions.assertEquals(data.nickname, "nickname")
        }
    }

    @Test
    fun updateUserData() {
        runBlockingTest {
            repository = Mockito.mock(AuthRepository::class.java)
            Mockito.`when`(
                repository.updateUserData(
                    username = "username",
                    nickname = "nickname",
                    password = "password",
                    newPassword = "newpassword"
                )
            ).then {
                return@then Response.Failure(Error.NetworkConnectionError())
            }
            val result = repository.updateUserData(
                username = "username",
                nickname = "nickname",
                password = "password",
                newPassword = "newpassword"
            )
            Assertions.assertTrue(result is Response.Failure)

            Mockito.`when`(
                repository.updateUserData(
                    username = "username1",
                    nickname = "nickname1",
                    password = "password1",
                    newPassword = "newpassword1"
                )
            ).then {
                return@then Response.Success(
                    UpdateUserDataResponse(
                        Auth(
                            id = 0,
                            username = "username1",
                            nickname = "nickname1",
                            accessToken = "newAccessToken"
                        )
                    )
                )
            }
            val result2 = repository.updateUserData(
                username = "username1",
                nickname = "nickname1",
                password = "password1",
                newPassword = "newpassword1"
            )
            Assertions.assertTrue(result2 is Response.Success)
            val data = (result2 as Response.Success).successData.response
            Assertions.assertEquals(data.username, "username1")
            Assertions.assertEquals(data.nickname, "nickname1")
            Assertions.assertEquals(data.accessToken, "newAccessToken")
        }
    }
}