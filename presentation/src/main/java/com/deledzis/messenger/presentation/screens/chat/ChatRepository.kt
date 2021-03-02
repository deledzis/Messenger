package com.deledzis.messenger.presentation.screens.chat

import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.presentation.base.BaseRepository
import com.deledzis.messenger.remote.model.messages.AddMessageRequest

class ChatRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getChat(id: Int): Chat? {
        return safeApiCall { api.getChat(chatId = id) }
    }

    suspend fun sendTextMessage(
        chatId: Int,
        authorId: Int,
        type: Boolean,
        content: String
    ): BaseResponse? {
        return safeApiCall {
            api.sendMessageToChat(
                chatId = chatId,
                request = AddMessageRequest(
                    chatId = chatId,
                    authorId = authorId,
                    type = type,
                    content = content
                )
            )
        }
    }
}