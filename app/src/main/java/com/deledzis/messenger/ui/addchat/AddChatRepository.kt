package com.deledzis.messenger.ui.addchat

import com.deledzis.messenger.api.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.data.model.users.Users

class AddChatRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getAvailableUsers(searchText: String): Users? {
        // TODO to be fixed when backend works
        return null /*safeApiCall(
            call = { api.getUsersAvailable(search = searchText) },
            errorMessage = "Error while getting available users, param = $searchText"
        )*/
    }

    suspend fun addChat(userId: Int, interlocutorId: Int): ChatReduced? {
        // TODO to be fixed when backend works
        return null /*safeApiCall(
            call = { api.addChat(request = AddChatRequest(
                authorId = userId,
                interlocutorId = interlocutorId,
            )) },
            errorMessage = "Error while adding chat"
        )*/
    }
}