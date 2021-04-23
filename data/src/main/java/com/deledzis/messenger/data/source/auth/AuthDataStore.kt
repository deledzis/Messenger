package com.deledzis.messenger.data.source.auth

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.auth.AuthEntity

/**
 * Interface defining methods for the data operations related to [AuthEntity].
 * This is to be implemented by external data source layers, setting the requirements for the
 * operations that need to be implemented
 */
interface AuthDataStore {

    suspend fun login(
        username: String,
        password: String
    ): Response<AuthEntity, Error>

    suspend fun register(
        username: String,
        nickname: String?,
        password: String
    ): Response<AuthEntity, Error>

    suspend fun updateUserData(
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): Response<AuthEntity, Error>

    suspend fun deleteAccount(username: String): Response<ServerMessageResponseEntity, Error>

}