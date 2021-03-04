package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.AuthRemoteImpl
import com.deledzis.messenger.remote.ChatsRemoteImpl
import com.deledzis.messenger.remote.TestData
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChatsRemoteImplTest {

    var apiService: ApiService = MockNetworkModule().buildService()

    var chatsRemoteImpl: ChatsRemoteImpl = ChatsRemoteImpl(apiService)

    @Test
    fun getChatsTest() = runBlocking {
        val chats = chatsRemoteImpl.getChats()
        assertEquals(chats, TestData.chats)
    }

    @Test
    fun addChatTest() = runBlocking {
        val chat = chatsRemoteImpl.addChat(TestData.chat.interlocutorId)
        assertEquals(chat, TestData.chat)
    }

}