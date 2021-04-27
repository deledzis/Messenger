package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.chats.ChatsRepositoryImpl
import com.deledzis.messenger.data.repository.messages.MessagesRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.infrastructure.di.UtilsModule
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
class MessagesRepositoryIntegrationTest {

    @Inject
    lateinit var authRepository: AuthRepositoryImpl

    @Inject
    lateinit var chatsRepository: ChatsRepositoryImpl

    @Inject
    lateinit var messagesRepository: MessagesRepositoryImpl

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
    fun test1_getChatMessages() {
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

            val result4 =
                messagesRepository.getChatMessages(chatId = data3.items[0].id, search = "")
            assertThat(result4 is Response.Success).isTrue()
            val data4 = (result4 as Response.Success).successData.response
            assertThat(data4.items).isNotEmpty()

            // rollback
            authRepository.login("test", "testtest").handleResult {
                userData.saveAuthUser(it.response)
            }
        }
    }

    @Test
    fun test2_sendMessageToChat() {
        runBlocking {
            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data = (result1 as Response.Success).successData.response
            assertThat(data.items).isEmpty()

            val result2 = chatsRepository.addChat(interlocutorId = 1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response

            val result3 = messagesRepository.getChatMessages(chatId = data2.id, search = "")
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isEmpty()

            val result4 = messagesRepository.sendMessageToChat(
                chatId = data2.id,
                content = "test",
                type = Message.TYPE_TEXT
            )
            assertThat(result4 is Response.Success).isTrue()

            val result5 = messagesRepository.getChatMessages(chatId = data2.id, search = "")
            assertThat(result5 is Response.Success).isTrue()
            val data4 = (result5 as Response.Success).successData.response
            assertThat(data4.items).isNotEmpty()
            assertThat(data4.items.last().content).isEqualTo("test")
            assertThat(data4.items.last().type).isEqualTo(Message.TYPE_TEXT)

            val result6 = messagesRepository.getChatMessages(chatId = data2.id, search = "test")
            assertThat(result6 is Response.Success).isTrue()
            val data5 = (result6 as Response.Success).successData.response
            assertThat(data5.items).isNotEmpty()
            assertThat(data5.items.size).isEqualTo(1)

            val result7 = messagesRepository.getChatMessages(chatId = data2.id, search = "asd")
            assertThat(result7 is Response.Success).isTrue()
            val data6 = (result7 as Response.Success).successData.response
            assertThat(data6.items).isEmpty()
        }
    }

    @Test
    fun test3_deleteMessage() {
        runBlocking {
            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data = (result1 as Response.Success).successData.response
            assertThat(data.items).isEmpty()

            val result2 = chatsRepository.addChat(interlocutorId = 1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response

            val result3 = messagesRepository.getChatMessages(chatId = data2.id, search = "")
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isEmpty()

            val result4 = messagesRepository.sendMessageToChat(
                chatId = data2.id,
                content = "test",
                type = Message.TYPE_TEXT
            )
            assertThat(result4 is Response.Success).isTrue()

            val result5 = messagesRepository.getChatMessages(chatId = data2.id, search = "")
            assertThat(result5 is Response.Success).isTrue()
            val data4 = (result5 as Response.Success).successData.response
            assertThat(data4.items).isNotEmpty()

            val result6 = messagesRepository.deleteMessage(messageId = data4.items.last().id)
            assertThat(result6 is Response.Success).isTrue()
            val data5 = (result6 as Response.Success).successData.response
            assertThat(data5.errorCode).isEqualTo(0)

            val result7 = messagesRepository.getChatMessages(chatId = data2.id, search = "")
            assertThat(result7 is Response.Success).isTrue()
            val data6 = (result7 as Response.Success).successData.response
            assertThat(data6.items).isEmpty()
        }
    }
}