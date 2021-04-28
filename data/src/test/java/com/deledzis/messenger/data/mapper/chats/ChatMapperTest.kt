package com.deledzis.messenger.data.mapper.chats

import com.deledzis.messenger.data.mapper.messages.MessageMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChatMapperTest {
    val chatMapper = ChatMapper(messageMapper = MessageMapper())

    @Test
    fun chatToChatEntityTest() {
        val chatEntity = chatMapper.mapToEntity(ChatsTestData.chat)
        assertEquals(chatEntity.id, ChatsTestData.chat.id)
        assertEquals(chatEntity.title, ChatsTestData.chat.title)
        assertEquals(chatEntity.interlocutorId, ChatsTestData.chat.interlocutorId)
        assertEquals(chatEntity.lastMessage, ChatsTestData.chat.lastMessage)
    }

    @Test
    fun chatEntityToChatTest() {
        val chat = chatMapper.mapFromEntity(ChatsTestData.chatEntity)
        assertEquals(chat.id, ChatsTestData.chatEntity.id)
        assertEquals(chat.title, ChatsTestData.chatEntity.title)
        assertEquals(chat.interlocutorId, ChatsTestData.chatEntity.interlocutorId)
        assertEquals(chat.lastMessage, ChatsTestData.chatEntity.lastMessage)
    }
}