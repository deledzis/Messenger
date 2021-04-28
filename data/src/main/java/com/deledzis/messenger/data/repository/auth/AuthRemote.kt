package com.deledzis.messenger.data.repository.auth

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.auth.AuthEntity

interface AuthRemote {
    suspend fun login(
        username: String,
        password: String
    ): AuthEntity

    suspend fun register(
        username: String,
        nickname: String?,
        password: String
    ): AuthEntity

    suspend fun updateUserData(
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): AuthEntity

    suspend fun deleteAccount(username: String): ServerMessageResponseEntity
}