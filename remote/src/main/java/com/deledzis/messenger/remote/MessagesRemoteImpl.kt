package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity
import com.deledzis.messenger.data.repository.messages.MessagesRemote
import com.deledzis.messenger.remote.model.messages.AddMessageRequest

class MessagesRemoteImpl(private val apiService: ApiService) : MessagesRemote {

    override suspend fun getChatMessages(chatId: Int, search: String?): MessagesEntity =
        apiService.getChatMessages(chatId = chatId, search = search)

    override suspend fun sendMessage(
        chatId: Int,
        type: Int,
        content: String
    ): ServerMessageResponseEntity =
        apiService.sendMessageToChat(
            chatId = chatId,
            request = AddMessageRequest(type = type, content = content)
        )
}