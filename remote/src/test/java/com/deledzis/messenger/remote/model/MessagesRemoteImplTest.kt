package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.MessagesRemoteImpl
import com.deledzis.messenger.remote.TestData
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
                chatId = TestData.messages.items[0].chatId,
                search = ""
            )
            assertEquals(messagesAll, TestData.messages)
            val messagesWithSearch = messagesRemoteImpl.getChatMessages(
                chatId = TestData.messages.items[0].chatId,
                search = "asdjlksajdklsad"
            )
            assertNotSame(messagesWithSearch, messagesAll)
        }
    }

    @Test
    fun sendMessageTest() {
        runBlocking {
            val serverMessageResponse = messagesRemoteImpl.sendMessage(
                chatId = TestData.messages.items[0].chatId,
                type = TestData.messages.items[0].type,
                content = TestData.messages.items[0].content ?: ""
            )
            assertEquals(serverMessageResponse.errorCode, 0)
        }
    }
}