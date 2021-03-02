package com.deledzis.messenger.data.source.chats

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity

/**
 * Interface defining methods for the data operations related to [ChatEntity].
 * This is to be implemented by external data source layers, setting the requirements for the
 * operations that need to be implemented
 */
interface ChatsDataStore {

    suspend fun getChats(): Response<ChatsEntity, Error>

    suspend fun addChat(interlocutorId: Int): Response<ChatEntity, Error>

}