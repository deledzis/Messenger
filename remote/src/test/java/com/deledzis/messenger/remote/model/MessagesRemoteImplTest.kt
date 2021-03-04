package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.MessagesRemoteImpl
import com.deledzis.messenger.remote.TestData
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MessagesRemoteImplTest {
    var apiService: ApiService = MockNetworkModule().buildService()

    var messagesRemoteImpl: MessagesRemoteImpl = MessagesRemoteImpl(apiService)

    @Test
    fun getChatMessagesTest() = runBlocking {
        val messages = messagesRemoteImpl.getChatMessages(TestData.messages.items[0].chatId, "")
        Assertions.assertEquals(messages, TestData.messages)
    }

    @Test
    fun sendMessageTest() = runBlocking {
        val serverMessageResponse = messagesRemoteImpl.sendMessage(
            TestData.messages.items[0].chatId,
            TestData.messages.items[0].type,
            TestData.messages.items[0].content ?: ""
        )
        Assertions.assertEquals(serverMessageResponse, TestData.serverMessageResponse)
    }
}