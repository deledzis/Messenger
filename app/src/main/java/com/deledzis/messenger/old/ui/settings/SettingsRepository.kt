package com.deledzis.messenger.old.ui.settings

import com.deledzis.messenger.domain.model.request.auth.UpdateUserRequest
import com.deledzis.messenger.old.data.model.auth.AuthorizedUser
import com.deledzis.messenger.presentation.base.BaseRepository

class SettingsRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun updateUserData(
        id: Int,
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): AuthorizedUser? {
        return safeApiCall {
            api.updateUserData(
                UpdateUserRequest(
                    userId = id,
                    username = username,
                    nickname = nickname,
                    password = password,
                    newPassword = newPassword
                )
            )
        }
    }
}