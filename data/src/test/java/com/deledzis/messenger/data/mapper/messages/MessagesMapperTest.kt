package com.deledzis.messenger.data.mapper.messages

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessagesMapperTest {
    val messagesMapper = MessagesMapper(MessageMapper())

    @Test
    fun messagesToMessagesEntityTest() {
        val messagesEntity = messagesMapper.mapToEntity(MessagesTestData.messages)
        assertEquals(messagesEntity.items.size, MessagesTestData.messages.items.size)
        messagesEntity.items.zip(MessagesTestData.messages.items).forEach{ pair ->
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(pair.first.authorId, pair.second.authorId)
            assertEquals(pair.first.authorName, pair.second.authorName)
            assertEquals(pair.first.chatId, pair.second.chatId)
            assertEquals(pair.first.content, pair.second.content)
            assertEquals(pair.first.type, pair.second.type)
            assertEquals(pair.first.date, pair.second.date)
        }
    }

    @Test
    fun messagesEntityToMessagesTest() {
        val messages = messagesMapper.mapFromEntity(MessagesTestData.messagesEntity)
        assertEquals(messages.items.size, MessagesTestData.messagesEntity.items.size)
        messages.items.zip(MessagesTestData.messagesEntity.items).forEach{ pair ->
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(pair.first.authorId, pair.second.authorId)
            assertEquals(pair.first.authorName, pair.second.authorName)
            assertEquals(pair.first.chatId, pair.second.chatId)
            assertEquals(pair.first.content, pair.second.content)
            assertEquals(pair.first.type, pair.second.type)
            assertEquals(pair.first.date, pair.second.date)
        }
    }
}