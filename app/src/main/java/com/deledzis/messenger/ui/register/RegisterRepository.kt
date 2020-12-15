package com.deledzis.messenger.ui.register

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.auth.AuthorizedUser
import com.deledzis.messenger.data.model.auth.RegisterUserRequest
import com.deledzis.messenger.data.remote.ApiInterface

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