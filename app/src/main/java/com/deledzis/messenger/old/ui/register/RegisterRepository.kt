package com.deledzis.messenger.old.ui.register

import com.deledzis.messenger.old.data.model.auth.AuthorizedUser
import com.deledzis.messenger.presentation.base.BaseRepository

class RegisterRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun register(username: String, nickname: String?, password: String): AuthorizedUser? {
        return safeApiCall {
            api.registerUser(
                request = RegisterUserRequest(
                    username = username,
                    nickname = nickname,
                    password = password
                )
            )
        }
    }
}