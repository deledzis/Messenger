package com.deledzis.messenger.ui.chat

import com.deledzis.messenger.api.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Chat

class ChatRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getChat(id: Int): Chat? {
        // TODO to be fixed when backend works
        return null/*safeApiCall(
            call = { api.getChat(id = id) },
            errorMessage = "Error while getting chat"
        )*/
    }

    suspend fun sendTextMessage(chatId: Int, authorId: Int, type: Boolean, content: String): Any? {
        return null /*safeApiCall(
            call = {
                api.sendMessageToChat(
                    request = SendMessageRequest(
                        chatId = chatId,
                        authorId = authorId,
                        type = type,
                        content = content
                    )
                )
            },
            errorMessage = "Error while sending message"
        )*/
    }
}