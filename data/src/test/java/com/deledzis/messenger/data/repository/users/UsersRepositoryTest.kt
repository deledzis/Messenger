package com.deledzis.messenger.data.repository.users

import com.deledzis.messenger.common.extensions.times
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.eq
import com.deledzis.messenger.domain.model.entity.user.User
import com.deledzis.messenger.domain.model.entity.user.Users
import com.deledzis.messenger.domain.model.response.user.GetUserResponse
import com.deledzis.messenger.domain.model.response.user.GetUsersResponse
import com.deledzis.messenger.domain.repository.UsersRepository
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
internal class UsersRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: UsersRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getUser() {
        runBlockingTest {
            repository = Mockito.mock(UsersRepository::class.java)
            Mockito.`when`(repository.getUser(Mockito.anyInt())).then {
                return@then Response.Success(
                    GetUserResponse(Mockito.mock(User::class.java))
                )
            }

            val result = repository.getUser(id = 0)
            Mockito.verify(repository).getUser(eq(0))
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertNotNull(data)
            Assertions.assertEquals(data.id, 0)
        }
    }

    @Test
    fun getUsers() {
        runBlockingTest {
            repository = Mockito.mock(UsersRepository::class.java)
            Mockito.`when`(repository.getUsers(Mockito.anyString())).then {
                return@then Response.Success(
                    GetUsersResponse(
                        Users(
                            items = listOf(Mockito.mock(User::class.java)).times(5)
                        )
                    )
                )
            }

            val result = repository.getUsers(search = "test")
            Mockito.verify(repository).getUsers(eq("test"))
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertNotNull(data)
            Assertions.assertEquals(data.items.size, 5)
        }
    }
}