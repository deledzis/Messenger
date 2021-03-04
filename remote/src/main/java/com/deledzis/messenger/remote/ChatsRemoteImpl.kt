package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.data.repository.chats.ChatsRemote
import com.deledzis.messenger.remote.model.chats.AddChatRequest

class ChatsRemoteImpl(private val apiService: ApiService) : ChatsRemote {

    override suspend fun getChats(): ChatsEntity = apiService.getChats()

    override suspend fun addChat(interlocutorId: Int): ChatEntity =
        apiService.addChat(request = AddChatRequest(interlocutorId = interlocutorId))
}