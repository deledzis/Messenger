package com.deledzis.messenger.ui.chat

import com.deledzis.messenger.data.remote.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Chat
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
                    id = chatId,
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

    suspend fun sendImageMessage(
        chatId: Int,
        authorId: RequestBody,
        image: MultipartBody.Part
    ): Any? {
        return null /*safeApiCall(
            call = {
                api.sendPhotoToChat(
                    id = chatId,
                    authorId = authorId,
                    image = image
                )
            },
            errorMessage = "Error while sending image message"
        )*/
    }
}