package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.data.repository.auth.AuthRemote
import com.deledzis.messenger.remote.model.auth.AuthUserRequest
import com.deledzis.messenger.remote.model.auth.RegisterUserRequest
import com.deledzis.messenger.remote.model.user.DeleteUserRequest
import com.deledzis.messenger.remote.model.user.UpdateUserRequest

class AuthRemoteImpl(private val apiService: ApiService) : AuthRemote {

    override suspend fun login(username: String, password: String): AuthEntity =
        apiService.login(
            request = AuthUserRequest(
                username = username,
                password = password
            )
        )

    override suspend fun register(
        username: String,
        nickname: String?,
        password: String
    ): AuthEntity = apiService.register(
        request = RegisterUserRequest(
            username = username,
            nickname = nickname,
            password = password
        )
    )

    override suspend fun updateUserData(
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): AuthEntity = apiService.updateUserData(
        request = UpdateUserRequest(
            username = username,
            nickname = nickname,
            password = password,
            newPassword = newPassword
        )
    )

    override suspend fun deleteAccount(username: String): ServerMessageResponseEntity =
        apiService.deleteAccount(request = DeleteUserRequest(username = username))
}