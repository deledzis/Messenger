package com.deledzis.messenger.ui.chats

import com.deledzis.messenger.api.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Chats

class ChatsRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getChats(): Chats? {
        return safeApiCall(
            call = { api.getChats() },
            errorMessage = "Error while getting chats"
        )
    }
}