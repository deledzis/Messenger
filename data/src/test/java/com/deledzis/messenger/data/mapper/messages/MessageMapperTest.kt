package com.deledzis.messenger.data.mapper.messages

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageMapperTest {
    val messageMapper = MessageMapper()

    @Test
    fun messageToMessageEntityTest() {
        val messageEntity = messageMapper.mapToEntity(MessagesTestData.message)
        assertEquals(messageEntity.id, MessagesTestData.message.id)
        assertEquals(messageEntity.authorId, MessagesTestData.message.authorId)
        assertEquals(messageEntity.authorName, MessagesTestData.message.authorName)
        assertEquals(messageEntity.chatId, MessagesTestData.message.chatId)
        assertEquals(messageEntity.content, MessagesTestData.message.content)
        assertEquals(messageEntity.date, MessagesTestData.message.date)
        assertEquals(messageEntity.type, MessagesTestData.message.type)
    }

    @Test
    fun messageEntityToMessageTest() {
        val message = messageMapper.mapFromEntity(MessagesTestData.messageEntity)
        assertEquals(message.id, MessagesTestData.messageEntity.id)
        assertEquals(message.authorId, MessagesTestData.messageEntity.authorId)
        assertEquals(message.authorName, MessagesTestData.messageEntity.authorName)
        assertEquals(message.chatId, MessagesTestData.messageEntity.chatId)
        assertEquals(message.content, MessagesTestData.messageEntity.content)
        assertEquals(message.date, MessagesTestData.messageEntity.date)
        assertEquals(message.type, MessagesTestData.messageEntity.type)
    }
}