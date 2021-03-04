package com.deledzis.messenger.data.mapper.chats

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.mapper.messages.MessageMapper
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.domain.model.entity.chats.Chat
import javax.inject.Inject

class ChatMapper @Inject constructor(
    private val messageMapper: MessageMapper
) : Mapper<ChatEntity, Chat> {
    override fun mapFromEntity(type: ChatEntity): Chat {
        return Chat(
            id = type.id,
            title = type.title,
            interlocutorId = type.interlocutorId,
            lastMessage = type.lastMessage?.let { messageMapper.mapFromEntity(it) }
        )
    }

    override fun mapToEntity(type: Chat): ChatEntity {
        return ChatEntity(
            id = type.id,
            title = type.title,
            interlocutorId = type.interlocutorId,
            lastMessage = type.lastMessage?.let { messageMapper.mapToEntity(it) }
        )
    }
}