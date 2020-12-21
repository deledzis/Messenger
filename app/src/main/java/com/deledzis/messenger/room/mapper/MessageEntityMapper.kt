package com.deledzis.messenger.room.mapper

import com.deledzis.messenger.App
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.room.database.MessengerDatabase
import com.deledzis.messenger.room.model.MessageEntity

class MessageEntityMapper: EntityMapper<MessageEntity, Message> {
    override fun mapFromEntity(type: MessageEntity): Message {
        val userEntityDao = MessengerDatabase.getInstance(App.injector.context()).userEntityDao
        val authorEntity = userEntityDao.getUser(type.author)
        val author = UserEntityMapper().mapFromEntity((authorEntity))
        return Message(type.id,
                       type.type,
                       type.content,
                       type.date,
                       type.chatId,
                       author)
    }

    override fun mapToEntity(type: Message): MessageEntity {
        return MessageEntity(type.id,
                             type.type,
                             type.content,
                             type.date,
                             type.chatId,
                             type.author.id)
    }
}