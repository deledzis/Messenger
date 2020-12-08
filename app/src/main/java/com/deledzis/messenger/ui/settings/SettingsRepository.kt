package com.deledzis.messenger.ui.settings

import com.deledzis.messenger.api.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.UpdateUserRequest
import com.deledzis.messenger.data.model.chats.Messages

class SettingsRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun updateUserData(id: Int, username: String, nickname: String?, password: String?, new_password:String? ): Auth? {
        return safeApiCall(
            call = {
                api.updateUserData(
                    UpdateUserRequest(
                        userId = id,
                        username = username,
                        nickname = nickname,
                        password = password,
                        new_password = new_password
                    )
                )
            },
            errorMessage = "Error while login"
        )
    }
}