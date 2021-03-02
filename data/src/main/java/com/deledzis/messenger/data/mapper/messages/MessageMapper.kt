package com.deledzis.messenger.data.mapper.messages

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.model.messages.MessageEntity
import com.deledzis.messenger.domain.model.entity.messages.Message
import javax.inject.Inject

class MessageMapper @Inject constructor() : Mapper<MessageEntity, Message> {
    override fun mapFromEntity(type: MessageEntity): Message {
        return Message(
            id = type.id,
            type = type.type,
            content = type.content,
            date = type.date,
            chatId = type.chatId,
            authorId = type.authorId,
            authorName = type.authorName,
        )
    }

    override fun mapToEntity(type: Message): MessageEntity {
        return MessageEntity(
            id = type.id,
            type = type.type,
            content = type.content,
            date = type.date,
            chatId = type.chatId,
            authorId = type.authorId,
            authorName = type.authorName,
        )
    }
}