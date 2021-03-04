package com.deledzis.messenger.remote.model

import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.ChatsRemoteImpl
import com.deledzis.messenger.remote.TestData
import com.deledzis.messenger.remote.di.MockNetworkModule
import io.mockk.InternalPlatformDsl.toArray
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
            assertEquals(chats.items.size, TestData.chats.items.size)
            assertArrayEquals(chats.items.toTypedArray(), TestData.chats.items.toArray())
        }
    }

    @Test
    fun addChatTest() {
        runBlocking {
            val chat = chatsRemoteImpl.addChat(TestData.chat.interlocutorId)
            assertEquals(chat.id, TestData.chat.id)
            assertEquals(chat.title, TestData.chat.title)
            assertEquals(chat.interlocutorId, TestData.chat.interlocutorId)
            assertEquals(chat.lastMessage, TestData.chat.lastMessage)
        }
    }

}