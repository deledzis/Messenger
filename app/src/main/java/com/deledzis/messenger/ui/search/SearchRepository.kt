package com.deledzis.messenger.ui.search

import com.deledzis.messenger.data.remote.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Messages

class SearchRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun search(searchText: String): Messages? {
        // TODO to be fixed when backend works
        return null /*safeApiCall(
                call = { api.getMessagesByQuery(search = searchText) },
        errorMessage = "Error while getting search results, param = $searchText"
        )*/
    }
}