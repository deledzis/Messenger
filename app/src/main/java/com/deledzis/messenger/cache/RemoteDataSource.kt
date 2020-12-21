package com.deledzis.messenger.cache

import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.BaseResponse
import com.deledzis.messenger.data.model.chats.Chat
import com.deledzis.messenger.data.model.chats.Chats
import com.deledzis.messenger.data.model.chats.SendMessageRequest
import retrofit2.Response

class RemoteDataSource {
    val api = App.injector.api()

    suspend fun getChat(chatId: Int, search: String? = ""): Response<Chat> {
        return api.getChat(chatId = chatId)
    }

    suspend fun getChats(): Response<Chats> {
        return api.getChats()
    }

    suspend fun sendTextMessage(chatId: Int, authorId: Int, type: Boolean, content: String): Response<BaseResponse> {
        return api.sendMessageToChat(
                    chatId = chatId,
                    request = SendMessageRequest(
                    chatId = chatId,
                    authorId = authorId,
                    type = type,
                    content = content
                )
            )
    }
}