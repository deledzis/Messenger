package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.chats.ChatsRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.remote.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ChatsRepositoryIntegrationTest {

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
            authRepository.register(
                username = "test",
                nickname = null,
                password = "testtest"
            )
            authRepository.login(
                username = "test",
                password = "testtest"
            ).handleResult {
                userData.saveAuthUser(it.response)
            }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            authRepository.login("test", "testtest").handleResult {
                userData.saveAuthUser(it.response)
                authRepository.deleteAccount(username = "test")
            }
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun test1_getChats() {
        runBlocking {
            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data = (result1 as Response.Success).successData.response
            assertThat(data.items).isEmpty()

            val result2 = authRepository.login("username", "password")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            userData.saveAuthUser(data2)

            val result3 = chatsRepository.getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotEmpty()
            assertThat(data.items != data3.items).isTrue()

            val result4 = authRepository.login("test", "testtest")
            assertThat(result4 is Response.Success).isTrue()
            val data4 = (result4 as Response.Success).successData.response
            userData.saveAuthUser(data4)

            delay(1000)
        }
    }

    @Test
    fun test2_addChat1() {
        runBlocking {
            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()
            assertThat(data1.items.any { it.interlocutorId == 1 }).isFalse()

            val result2 = chatsRepository.addChat(1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response

            val result3 = chatsRepository.getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotEmpty()
            assertThat(data3.items.any { it.interlocutorId == 1 }).isTrue()

            val result4 = chatsRepository.deleteChat(chatId = data2.id)
            assertThat(result4 is Response.Success).isTrue()

            delay(1000)
        }
    }

    @Test
    fun test2_addChat999999() {
        runBlocking {
            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()
            assertThat(data1.items.any { it.interlocutorId == -999999 }).isFalse()

            val result2 = chatsRepository.addChat(-999999)
            assertThat(result2 is Response.Failure).isTrue()

            val result3 = chatsRepository.getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isEmpty()
            assertThat(data3.items.any { it.interlocutorId == -999999 }).isFalse()

            delay(1000)
        }
    }

    @Test
    fun test2_deleteChat() {
        runBlocking {
            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()
            assertThat(data1.items.any { it.interlocutorId == 1 }).isFalse()

            val result2 = chatsRepository.addChat(1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response

            val result3 = chatsRepository.getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotEmpty()
            assertThat(data3.items.any { it.interlocutorId == 1 }).isTrue()

            val result4 = chatsRepository.deleteChat(chatId = data2.id)
            assertThat(result4 is Response.Success).isTrue()
            val data4 = (result4 as Response.Success).successData.response
            assertThat(data4.errorCode).isEqualTo(0)

            val result5 = chatsRepository.getChats()
            assertThat(result5 is Response.Success).isTrue()
            val data5 = (result5 as Response.Success).successData.response
            assertThat(data5.items).isEmpty()
            assertThat(data5.items.any { it.interlocutorId == 1 }).isFalse()

            delay(1000)
        }
    }
}