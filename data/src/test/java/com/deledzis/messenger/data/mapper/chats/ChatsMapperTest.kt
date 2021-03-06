package com.deledzis.messenger.data.mapper.chats

import com.deledzis.messenger.data.mapper.messages.MessageMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChatsMapperTest {
    val chatsMapper = ChatsMapper(ChatMapper(MessageMapper()))

    @Test
    fun chatsToChatsEntityTest() {
        val chatsEntity = chatsMapper.mapToEntity(ChatsTestData.chats)
        assertEquals(chatsEntity.items.size, ChatsTestData.chats.items.size)
        chatsEntity.items.zip(ChatsTestData.chats.items).forEach{ pair ->
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(pair.first.title, pair.second.title)
            assertEquals(pair.first.interlocutorId, pair.second.interlocutorId)
            assertEquals(pair.first.lastMessage, pair.second.lastMessage)
        }
    }

    @Test
    fun chatsEntityToChatsTest() {
        val chats = chatsMapper.mapFromEntity(ChatsTestData.chatsEntity)
        assertEquals(chats.items.size, ChatsTestData.chatsEntity.items.size)
        chats.items.zip(ChatsTestData.chatsEntity.items).forEach{ pair ->
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(pair.first.title, pair.second.title)
            assertEquals(pair.first.interlocutorId, pair.second.interlocutorId)
            assertEquals(pair.first.lastMessage, pair.second.lastMessage)
        }
    }
}