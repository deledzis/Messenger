package com.deledzis.messenger.data.repository.messages

import com.deledzis.messenger.common.extensions.times
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.eq
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.domain.model.entity.messages.Messages
import com.deledzis.messenger.domain.model.response.messages.GetChatMessagesResponse
import com.deledzis.messenger.domain.model.response.messages.SendMessageResponse
import com.deledzis.messenger.domain.repository.MessagesRepository
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
internal class MessagesRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: MessagesRepository

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
    fun getChatMessages() {
        runBlockingTest {
            repository = Mockito.mock(MessagesRepository::class.java)
            Mockito.`when`(
                repository.getChatMessages(
                    Mockito.anyInt(),
                    eq("")
                )
            ).then {
                return@then Response.Success(
                    GetChatMessagesResponse(
                        Messages(items = listOf(Mockito.mock(Message::class.java)).times(5))
                    )
                )
            }
            Mockito.`when`(
                repository.getChatMessages(
                    Mockito.anyInt(),
                    eq("test")
                )
            ).then {
                return@then Response.Success(
                    GetChatMessagesResponse(
                        Messages(items = listOf(Mockito.mock(Message::class.java)))
                    )
                )
            }

            val result1 = repository.getChatMessages(chatId = 0, search = "")
            Assertions.assertTrue(result1 is Response.Success)
            val data1 = (result1 as Response.Success).successData.response
            Assertions.assertEquals(data1.items.size, 5)

            val result2 = repository.getChatMessages(chatId = 0, search = "test")
            Assertions.assertTrue(result2 is Response.Success)
            val data2 = (result2 as Response.Success).successData.response
            Assertions.assertEquals(data2.items.size, 1)
        }
    }

    @Test
    fun sendMessageToChat() {
        runBlockingTest {
            repository = Mockito.mock(MessagesRepository::class.java)
            Mockito.`when`(
                repository.sendMessageToChat(
                    Mockito.anyInt(),
                    Mockito.anyInt(),
                    Mockito.anyString()
                )
            ).then {
                return@then Response.Success(
                    SendMessageResponse(Mockito.mock(ServerMessageResponse::class.java))
                )
            }

            val result = repository.sendMessageToChat(chatId = 0, type = 0, content = "test")
            Mockito.verify(repository).sendMessageToChat(chatId = 0, type = 0, content = "test")
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertEquals(data.errorCode, 0)
        }
    }
}