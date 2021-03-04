package com.deledzis.messenger.data.source.messages

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity

/**
 * Interface defining methods for the data operations related to [MessagesEntity].
 * This is to be implemented by external data source layers, setting the requirements for the
 * operations that need to be implemented
 */
interface MessagesDataStore {

    suspend fun getChatMessages(chatId: Int, search: String): Response<MessagesEntity, Error>

    suspend fun sendMessage(
        chatId: Int,
        type: Int,
        content: String
    ): Response<ServerMessageResponseEntity, Error>

}