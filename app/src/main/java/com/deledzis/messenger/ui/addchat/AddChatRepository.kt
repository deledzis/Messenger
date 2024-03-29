package com.deledzis.messenger.ui.addchat

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.AddChatRequest
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.data.model.users.Users
import com.deledzis.messenger.data.remote.ApiInterface

class AddChatRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getAvailableUsers(searchText: String): Users? {
        return safeApiCall { api.getUsersAvailable(search = searchText) }
    }

    suspend fun addChat(userId: Int, interlocutorId: Int): ChatReduced? {
        return safeApiCall {
            api.addChat(
                request = AddChatRequest(
                    authorId = userId,
                    interlocutorId = interlocutorId,
                )
            )
        }
    }
}