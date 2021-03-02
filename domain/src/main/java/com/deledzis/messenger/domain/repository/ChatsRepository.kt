package com.deledzis.messenger.domain.repository

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.response.chats.AddChatResponse
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse

/**
 * Interface defining methods for how the Data layer can pass data to and from the Domain layer.
 * This is to be implemented by the Data layer, setting the requirements for the
 * operations that need to be implemented
 */
interface ChatsRepository {
    suspend fun getChats(): Response<GetChatsResponse, Error>
    suspend fun addChat(interlocutorId: Int): Response<AddChatResponse, Error>
}