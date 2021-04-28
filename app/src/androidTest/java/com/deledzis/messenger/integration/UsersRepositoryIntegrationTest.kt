package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.chats.ChatsRepositoryImpl
import com.deledzis.messenger.data.repository.users.UsersRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.domain.model.response.auth.DeleteAccountResponse
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.domain.model.response.user.GetUserResponse
import com.deledzis.messenger.domain.model.response.user.GetUsersResponse
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.presentation.base.BaseViewModel.Companion.asHttpError
import com.deledzis.messenger.presentation.base.BaseViewModel.Companion.isServerUnavailableError
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class UsersRepositoryIntegrationTest {

    companion object {

        private const val MAX_RETRY_COUNT: Int = 5

    }

    @Inject
    lateinit var authRepository: AuthRepositoryImpl

    @Inject
    lateinit var usersRepository: UsersRepositoryImpl

    @Inject
    lateinit var chatsRepository: ChatsRepositoryImpl

    @Inject
    lateinit var userData: UserData

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val component = DaggerTestAppComponent.builder()
            .testCacheModule(TestCacheModule())
            .testNetworkModule(TestNetworkModule())
            .testRepositoriesModule(TestRepositoriesModule())
            .utilsModule(UtilsModule())
            .testAppModule(TestAppModule(context))
            .build()
        component.into(this)

        runBlocking {
            userData.saveAuthUser(null)
            register(
                username = "test",
                nickname = null,
                password = "testtest"
            )
            login("test", "testtest")?.handleResult {
                userData.saveAuthUser(it.response)
            }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            login("test", "testtest")?.handleResult {
                userData.saveAuthUser(it.response)
                deleteAccount(username = "test")
            }
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun test1_getUser() {
        runBlocking {
            val result1 = getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()

            val result2 = getUsers(search = "sasha")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            assertThat(data2.items).isNotEmpty()
            assertThat(data2.items[0].id).isNotNull()

            val result3 = getUser(id = data2.items[0].id!!)
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.id).isGreaterThan(0)

            val result4 = getUser(-999999)
            assertThat(result4 is Response.Failure).isTrue()
        }
    }

    @Test
    fun test2_getUsers() {
        runBlocking {
            val result1 = getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()

            val result2 = getUsers(search = "test")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            assertThat(data2.items).isNotEmpty()
            assertThat(data2.items[0].id).isNotNull()

            val result3 = getUsers(search = "fasvdvfdvfd")
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isEmpty()
        }
    }

    private suspend fun login(
        username: String,
        password: String,
        count: Int = 1
    ): Response<LoginResponse, Error>? {
        var response: Response<LoginResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            authRepository.login(username, password).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        login(username, password, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun register(
        username: String,
        nickname: String? = null,
        password: String,
        count: Int = 1
    ): Response<RegisterResponse, Error>? {
        var response: Response<RegisterResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            authRepository.register(username, nickname, password).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        register(username, nickname, password, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun deleteAccount(
        username: String,
        count: Int = 1
    ): Response<DeleteAccountResponse, Error>? {
        var response: Response<DeleteAccountResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            authRepository.deleteAccount(username).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        deleteAccount(username, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun getChats(count: Int = 1): Response<GetChatsResponse, Error>? {
        var response: Response<GetChatsResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            chatsRepository.getChats().handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        getChats(count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun getUser(id: Int, count: Int = 1): Response<GetUserResponse, Error>? {
        var response: Response<GetUserResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            usersRepository.getUser(id).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        getUser(id, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun getUsers(
        search: String? = null,
        count: Int = 1
    ): Response<GetUsersResponse, Error>? {
        var response: Response<GetUsersResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            usersRepository.getUsers(search).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        getUsers(search, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

}