package com.deledzis.messenger.old.ui.chats

import com.deledzis.messenger.domain.model.entity.chats.Chats
import com.deledzis.messenger.presentation.base.BaseRepository

class ChatsRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun getChats(): Chats? {
        return safeApiCall { api.getChats() }
    }
}