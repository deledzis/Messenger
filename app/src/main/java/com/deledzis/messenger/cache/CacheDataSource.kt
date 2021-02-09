package com.deledzis.messenger.cache

import com.deledzis.messenger.App
import com.deledzis.messenger.data.model.chats.*
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.room.database.MessengerDatabase
import com.deledzis.messenger.room.mapper.ChatEntityMapper
import com.deledzis.messenger.room.mapper.MessageEntityMapper
import com.deledzis.messenger.room.mapper.UserEntityMapper

class CacheDataSource {
    private var chatEntityDao = MessengerDatabase.getInstance(App.injector.context()).chatEntityDao
    private var messageEntityDao = MessengerDatabase.getInstance(App.injector.context()).messageEntityDao
    private var userEntityDao = MessengerDatabase.getInstance(App.injector.context()).userEntityDao

    fun addChat(chat: ChatReduced){
        chatEntityDao.insertChat(ChatEntityMapper().mapToEntity(chat))
    }

    fun getChat(chatId: Int, search: String? = ""): Chat {
        return ChatEntityMapper().mapFromEntity(chatEntityDao.getChat(chatId))
    }

    fun getChats() : Chats {
        val chats = chatEntityDao.getAllChats().map { ChatEntityMapper().mapFromEntityToReduced(it) }
        return Chats(0, null, chats)
    }

    fun updateMessages(messages: List<Message>) {
        messageEntityDao.insertMessages(messages.map { MessageEntityMapper().mapToEntity(it) })
    }

    fun updateChats(chats: List<ChatReduced>){
        for (chat in chats)
            userEntityDao.insertUser(UserEntityMapper().mapToEntity(chat.interlocutor))
        chatEntityDao.updateChats(chats.map{ ChatEntityMapper().mapToEntity(it) })
    }

    fun insertUser(user: User) {
        userEntityDao.insertUser(UserEntityMapper().mapToEntity(user))
    }
}