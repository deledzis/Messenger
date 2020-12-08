package com.deledzis.messenger.ui.settings

import com.deledzis.messenger.api.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.chats.Messages

class SettingsRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun search(searchText: String): Messages? {
        // TODO to be fixed when backend works
        return null
    }
}