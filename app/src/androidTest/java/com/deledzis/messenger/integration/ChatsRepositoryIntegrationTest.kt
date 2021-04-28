package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.chats.ChatsRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.domain.model.response.auth.DeleteAccountResponse
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.model.response.chats.AddChatResponse
import com.deledzis.messenger.domain.model.response.chats.DeleteChatResponse
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.presentation.base.BaseViewModel.Companion.asHttpError
import com.deledzis.messenger.presentation.base.BaseViewModel.Companion.isServerUnavailableError
import com.deledzis.messenger.remote.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ChatsRepositoryIntegrationTest {

    companion object {

        private const val MAX_RETRY_COUNT: Int = 5

    }

    @Inject
    lateinit var authRepository: AuthRepositoryImpl

    @Inject
    lateinit var chatsRepository: ChatsRepositoryImpl

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var api: ApiService

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
                deleteAccount("test")
            }
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun test1_getChats() {
        runBlocking {
            val result1 = getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data = (result1 as Response.Success).successData.response
            assertThat(data.items).isEmpty()

            val result2 = login("username", "password")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            userData.saveAuthUser(data2)

            val result3 = getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotEmpty()
            assertThat(data.items != data3.items).isTrue()

            val result4 = login("test", "testtest")
            assertThat(result4 is Response.Success).isTrue()
            val data4 = (result4 as Response.Success).successData.response
            userData.saveAuthUser(data4)
        }
    }

    @Test
    fun test2_addChat1() {
        runBlocking {
            val result1 = getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()
            assertThat(data1.items.any { it.interlocutorId == 1 }).isFalse()

            val result2 = addChat(1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response

            val result3 = getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotEmpty()
            assertThat(data3.items.any { it.interlocutorId == 1 }).isTrue()

            val result4 = deleteChat(chatId = data2.id)
            assertThat(result4 is Response.Success).isTrue()
        }
    }

    @Test
    fun test3_addChat999999() {
        runBlocking {
            val result1 = getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()
            assertThat(data1.items.any { it.interlocutorId == -999999 }).isFalse()

            val result2 = addChat(-999999)
            assertThat(result2 is Response.Failure).isTrue()

            val result3 = getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isEmpty()
            assertThat(data3.items.any { it.interlocutorId == -999999 }).isFalse()
        }
    }

    @Test
    fun test4_deleteChat() {
        runBlocking {
            val result1 = getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()
            assertThat(data1.items.any { it.interlocutorId == 1 }).isFalse()

            val result2 = addChat(1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response

            val result3 = getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotEmpty()
            assertThat(data3.items.any { it.interlocutorId == 1 }).isTrue()

            val result4 = deleteChat(chatId = data2.id)
            assertThat(result4 is Response.Success).isTrue()
            val data4 = (result4 as Response.Success).successData.response
            assertThat(data4.errorCode).isEqualTo(0)

            val result5 = getChats()
            assertThat(result5 is Response.Success).isTrue()
            val data5 = (result5 as Response.Success).successData.response
            assertThat(data5.items).isEmpty()
            assertThat(data5.items.any { it.interlocutorId == 1 }).isFalse()
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

    private suspend fun addChat(
        interlocutorId: Int,
        count: Int = 1
    ): Response<AddChatResponse, Error>? {
        var response: Response<AddChatResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            chatsRepository.addChat(interlocutorId).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        addChat(interlocutorId, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun deleteChat(
        chatId: Int,
        count: Int = 1
    ): Response<DeleteChatResponse, Error>? {
        var response: Response<DeleteChatResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            chatsRepository.deleteChat(chatId).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        deleteChat(chatId, count + 1)
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