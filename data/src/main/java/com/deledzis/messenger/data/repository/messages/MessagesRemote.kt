package com.deledzis.messenger.data.repository.messages

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity

interface MessagesRemote {
    suspend fun getChatMessages(chatId: Int, search: String): MessagesEntity
    suspend fun sendMessage(chatId: Int, type: Int, content: String): ServerMessageResponseEntity
}