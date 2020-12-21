package com.deledzis.messenger.cache

import com.deledzis.messenger.App
import com.deledzis.messenger.data.model.chats.Chat
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.data.model.chats.Chats
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.room.database.MessengerDatabase
import com.deledzis.messenger.room.mapper.ChatEntityMapper
import com.deledzis.messenger.room.mapper.MessageEntityMapper
import com.deledzis.messenger.room.mapper.UserEntityMapper

class CacheDataSource {
    private var chatEntityDao = MessengerDatabase.getInstance(App.injector.context()).chatEntityDao
    private var messageEntityDao =
        MessengerDatabase.getInstance(App.injector.context()).messageEntityDao
    private var userEntityDao = MessengerDatabase.getInstance(App.injector.context()).userEntityDao

    fun addChat(chat: ChatReduced) {
        chatEntityDao.insertChat(ChatEntityMapper().mapToEntity(chat))
    }

    fun getChat(chatId: Int, search: String? = ""): Chat {
        return ChatEntityMapper().mapFromEntity(chatEntityDao.getChat(chatId))
    }

    fun getChats(): Chats {
        val chats =
            chatEntityDao.getAllChats().map { ChatEntityMapper().mapFromEntityToReduced(it) }
        return Chats(0, null, chats)
    }

    fun updateMessages(messages: List<Message>) {
        messageEntityDao.insertMessages(messages.map { MessageEntityMapper().mapToEntity(it) })
    }

    fun updateChats(chats: List<ChatReduced>) {
//        chatEntityDao.deleteAll()
        chatEntityDao.updateChats(chats.map { ChatEntityMapper().mapToEntity(it) })
        for (chat in chats) {
//            userEntityDao.delete(UserEntityMapper().mapToEntity(chat.interlocutor).id)
            userEntityDao.insertUser(UserEntityMapper().mapToEntity(chat.interlocutor))
        }
    }
}