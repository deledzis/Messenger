package com.deledzis.messenger

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.di.CacheModule
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.di.RepositoriesModule
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.repository.ChatsRepository
import com.deledzis.messenger.domain.repository.MessagesRepository
import com.deledzis.messenger.domain.repository.UsersRepository
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.di.NetworkModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class RepositoriesIntegrationTest {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var chatsRepository: ChatsRepository

    @Inject
    lateinit var messagesRepository: MessagesRepository

    @Inject
    lateinit var usersRepository: UsersRepository

    @Inject
    lateinit var userData: BaseUserData

    @Inject
    lateinit var api : ApiService

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val component = DaggerTestAppComponent.builder()
            .cacheModule(CacheModule())
            .networkModule(NetworkModule())
            .repositoriesModule(RepositoriesModule())
            .utilsModule(UtilsModule())
            .testAppModule(TestAppModule(context))
            .build()
        component.into(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun loginGetChatsGetMessagesSendMessage(){
        runBlocking {
            userData.saveAuthUser(null)
            val result = authRepository.login(
                username = "username",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val id = (result as Response.Success).successData.response.id
            val accessToken = result.successData.response.accessToken
            userData.saveAuthUser(Auth(id, "username", "password", accessToken))

            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val chats = (result1 as Response.Success).successData.response
            assertThat(chats.items).isNotNull()
            assertThat(chats.items).isNotEmpty()
            val chatId = chats.items[0].id

            val result2 = messagesRepository.getChatMessages(chatId, "")
            assertThat(result2 is Response.Success).isTrue()
            val messages = (result2 as Response.Success).successData.response
            assertThat(messages.items).isNotNull()
            assertThat(messages.items).isNotEmpty()

            val result2_1 = messagesRepository.getChatMessages(-1, "")
            assertThat(result2_1 is Response.Failure).isTrue()

            val result3 = messagesRepository.sendMessageToChat(chatId, 0, "something test")
            assertThat(result3 is Response.Success).isTrue()
            val data = (result3 as Response.Success).successData.response
            assertThat(data.errorCode).isEqualTo(0)
            val result4 = messagesRepository.sendMessageToChat(-1, 0, "something test")
            assertThat(result4 is Response.Failure).isTrue()

            userData.saveAuthUser(null)
        }
    }
}