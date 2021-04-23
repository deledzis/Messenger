package com.deledzis.messenger.domain.repository

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.response.auth.DeleteAccountResponse
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.model.response.auth.UpdateUserDataResponse

interface AuthRepository {

    suspend fun login(
        username: String,
        password: String
    ): Response<LoginResponse, Error>

    suspend fun register(
        username: String,
        nickname: String?,
        password: String
    ): Response<RegisterResponse, Error>

    suspend fun updateUserData(
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): Response<UpdateUserDataResponse, Error>

    suspend fun deleteAccount(username: String): Response<DeleteAccountResponse, Error>
}