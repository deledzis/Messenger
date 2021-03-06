package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.ChatsRemoteImpl
import com.deledzis.messenger.remote.RemoteTestData
import com.deledzis.messenger.remote.di.MockNetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChatsRemoteImplTest {

    var apiService: ApiService = MockNetworkModule().buildService()

    var chatsRemoteImpl: ChatsRemoteImpl = ChatsRemoteImpl(apiService)

    @Test
    fun getChatsTest() {
        runBlocking {
            val chats = chatsRemoteImpl.getChats()
            assertEquals(chats.items.size, RemoteTestData.chats.items.size)
            assertArrayEquals(chats.items.toTypedArray(), RemoteTestData.chats.items.toTypedArray())
        }
    }

    @Test
    fun addChatTest() {
        runBlocking {
            val chat = chatsRemoteImpl.addChat(RemoteTestData.chat.interlocutorId)
            assertEquals(chat.id, RemoteTestData.chat.id)
            assertEquals(chat.title, RemoteTestData.chat.title)
            assertEquals(chat.interlocutorId, RemoteTestData.chat.interlocutorId)
            assertEquals(chat.lastMessage, RemoteTestData.chat.lastMessage)
        }
    }

}