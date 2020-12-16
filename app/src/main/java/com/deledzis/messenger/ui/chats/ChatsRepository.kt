package com.deledzis.messenger.ui.chats

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Chats
import com.deledzis.messenger.data.remote.ApiInterface

class ChatsRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getChats(): Chats? {
        return safeApiCall { api.getChats() }
    }
}