package com.deledzis.messenger.data.repository.chats

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity

interface ChatsRemote {

    suspend fun getChats(): ChatsEntity

    suspend fun addChat(interlocutorId: Int): ChatEntity

    suspend fun deleteChat(chatId: Int): ServerMessageResponseEntity

}