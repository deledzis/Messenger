package com.deledzis.messenger.ui.search

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Chat
import com.deledzis.messenger.data.remote.ApiInterface

class SearchRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun search(chatId: Int, searchText: String): Chat? {
        return safeApiCall(
            call = { api.getChat(chatId = chatId, search = searchText) },
            errorMessage = "Error while getting search results, param = $searchText"
        )
    }
}