package com.deledzis.messenger.data.repository.chats

import com.deledzis.messenger.common.extensions.times
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.entity.chats.Chats
import com.deledzis.messenger.domain.model.response.chats.AddChatResponse
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.domain.repository.ChatsRepository
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
internal class ChatsRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: ChatsRepository

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
    fun getChats() {
        runBlockingTest {
            repository = Mockito.mock(ChatsRepository::class.java)
            Mockito.`when`(repository.getChats()).then {
                return@then Response.Success(
                    GetChatsResponse(
                        Chats(items = listOf(Mockito.mock(Chat::class.java)).times(3))
                    )
                )
            }
            val result = repository.getChats()
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertEquals(data.items.size, 3)
        }
    }

    @Test
    fun addChat() {
        runBlockingTest {
            repository = Mockito.mock(ChatsRepository::class.java)
            Mockito.`when`(repository.addChat(interlocutorId = Mockito.anyInt())).then {
                return@then Response.Success(
                    AddChatResponse(
                        Chat(
                            id = 0,
                            title = "test",
                            interlocutorId = 5,
                            lastMessage = null
                        )
                    )
                )
            }
            val result = repository.addChat(interlocutorId = 5)
            Assertions.assertTrue(result is Response.Success)
            val data = (result as Response.Success).successData.response
            Assertions.assertEquals(data.id, 0)
            Assertions.assertEquals(data.title, "test")
            Assertions.assertEquals(data.interlocutorId, 5)
            Assertions.assertNull(data.lastMessage)
        }
    }
}