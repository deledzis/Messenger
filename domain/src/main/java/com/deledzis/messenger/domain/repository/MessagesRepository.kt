package com.deledzis.messenger.domain.repository

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.response.messages.DeleteMessageResponse
import com.deledzis.messenger.domain.model.response.messages.GetChatMessagesResponse
import com.deledzis.messenger.domain.model.response.messages.SendMessageResponse

/**
 * Interface defining methods for how the Data layer can pass data to and from the Domain layer.
 * This is to be implemented by the Data layer, setting the requirements for the
 * operations that need to be implemented
 */
interface MessagesRepository {

    suspend fun getChatMessages(
        chatId: Int,
        search: String
    ): Response<GetChatMessagesResponse, Error>

    suspend fun sendMessageToChat(
        chatId: Int,
        type: Int,
        content: String
    ): Response<SendMessageResponse, Error>

    suspend fun deleteMessage(messageId: Int): Response<DeleteMessageResponse, Error>

}