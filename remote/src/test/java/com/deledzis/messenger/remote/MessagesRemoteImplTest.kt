package com.deledzis.messenger.remote

import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Test

class MessagesRemoteImplTest {
    var apiService: ApiService = MockNetworkModule().buildService()

    var messagesRemoteImpl: MessagesRemoteImpl = MessagesRemoteImpl(apiService)

    @Test
    fun getChatMessagesTest() {
        runBlocking {
            val messagesAll = messagesRemoteImpl.getChatMessages(
                chatId = RemoteTestData.messages.items[0].chatId,
                search = ""
            )
            assertEquals(messagesAll, RemoteTestData.messages)
            val messagesWithSearch = messagesRemoteImpl.getChatMessages(
                chatId = RemoteTestData.messages.items[0].chatId,
                search = "asdjlksajdklsad"
            )
            assertNotSame(messagesWithSearch, messagesAll)
        }
    }

    @Test
    fun sendMessageTest() {
        runBlocking {
            val serverMessageResponse = messagesRemoteImpl.sendMessage(
                chatId = RemoteTestData.messages.items[0].chatId,
                type = RemoteTestData.messages.items[0].type,
                content = RemoteTestData.messages.items[0].content ?: ""
            )
            assertEquals(serverMessageResponse.errorCode, 0)
        }
    }
}