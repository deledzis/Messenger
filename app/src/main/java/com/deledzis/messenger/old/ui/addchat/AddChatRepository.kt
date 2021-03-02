package com.deledzis.messenger.old.ui.addchat

import com.deledzis.messenger.domain.model.entity.chats.ChatReduced
import com.deledzis.messenger.domain.model.entity.user.Users
import com.deledzis.messenger.presentation.base.BaseRepository

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