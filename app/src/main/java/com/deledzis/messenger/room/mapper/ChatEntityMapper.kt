package com.deledzis.messenger.room.mapper

import com.deledzis.messenger.App
import com.deledzis.messenger.data.model.chats.Chat
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.room.database.MessengerDatabase
import com.deledzis.messenger.room.model.ChatEntity

class ChatEntityMapper : EntityMapper<ChatEntity, Chat> {
    override fun mapFromEntity(type: ChatEntity): Chat {
        val messageEntityDao =
            MessengerDatabase.getInstance(App.injector.context()).messageEntityDao
        val userEntityDao = MessengerDatabase.getInstance(App.injector.context()).userEntityDao
        val interlocutor =
            UserEntityMapper().mapFromEntity(userEntityDao.getUser(type.interlocutor))
        val messagesEntity = messageEntityDao.getChatMessages(type.id)
        val messages = messagesEntity.map { MessageEntityMapper().mapFromEntity(it) }
        return Chat(type.id, interlocutor, messages)
    }

    override fun mapToEntity(type: Chat): ChatEntity {
        return ChatEntity(type.id, type.interlocutor.id)
    }

    fun mapToEntity(type: ChatReduced): ChatEntity {
        return ChatEntity(type.id, type.interlocutor.id)
    }

    fun mapFromEntityToReduced(type: ChatEntity): ChatReduced {
        val messageEntityDao =
            MessengerDatabase.getInstance(App.injector.context()).messageEntityDao
        val userEntityDao = MessengerDatabase.getInstance(App.injector.context()).userEntityDao
        val interlocutor =
            UserEntityMapper().mapFromEntity(userEntityDao.getUser(type.interlocutor))
        val messageEntity = messageEntityDao.getLastMessage(type.id)
        val message = MessageEntityMapper().mapFromEntity(messageEntity)
        return ChatReduced(type.id, interlocutor, message)
    }
}