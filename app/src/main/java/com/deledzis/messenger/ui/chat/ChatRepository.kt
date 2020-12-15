package com.deledzis.messenger.ui.chat

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.BaseResponse
import com.deledzis.messenger.data.model.chats.Chat
import com.deledzis.messenger.data.model.chats.SendMessageRequest
import com.deledzis.messenger.data.remote.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChatRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getChat(id: Int): Chat? {
        return safeApiCall { api.getChat(chatId = id) }
    }

    suspend fun sendTextMessage(chatId: Int, authorId: Int, type: Boolean, content: String): BaseResponse? {
        return safeApiCall {
            api.sendMessageToChat(
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

    suspend fun sendImageMessage(
        chatId: Int,
        authorId: RequestBody,
        image: MultipartBody.Part
    ): BaseResponse? {
        return null /*safeApiCall(
            call = {
                api.sendPhotoToChat(
                    chatId = chatId,
                    authorId = authorId,
                    image = image
                )
            },
            errorMessage = "Error while sending image message"
        )*/
    }
}