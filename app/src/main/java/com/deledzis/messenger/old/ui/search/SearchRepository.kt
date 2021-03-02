package com.deledzis.messenger.old.ui.search

import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.presentation.base.BaseRepository

class SearchRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun search(chatId: Int, searchText: String): Chat? {
        return safeApiCall { api.getChat(chatId = chatId, search = searchText) }
    }
}