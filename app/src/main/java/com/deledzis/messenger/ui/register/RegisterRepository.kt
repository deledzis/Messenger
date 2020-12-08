package com.deledzis.messenger.ui.register

import com.deledzis.messenger.data.remote.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.RegisterUserRequest

class RegisterRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun register(username: String, nickname: String?, password: String): Auth? {
        return safeApiCall(
            call = {
                api.registerUser(
                    request = RegisterUserRequest(
                        username = username,
                        nickname = nickname,
                        password = password
                    )
                )
            },
            errorMessage = "Error while registering"
        )
    }
}