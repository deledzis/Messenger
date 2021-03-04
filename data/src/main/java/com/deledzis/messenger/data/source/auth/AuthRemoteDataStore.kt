package com.deledzis.messenger.data.source.auth

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.data.repository.auth.AuthRemote
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Implementation of the [AuthDataStore] interface to provide a means of communicating
 * with the remote data source
 */
class AuthRemoteDataStore @Inject constructor(private val remote: AuthRemote) :
    AuthDataStore {

    override suspend fun login(username: String, password: String): Response<AuthEntity, Error> {
        return try {
            val response = remote.login(
                username = username,
                password = password
            )
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }

    override suspend fun register(
        username: String,
        nickname: String?,
        password: String
    ): Response<AuthEntity, Error> {
        return try {
            val response = remote.register(
                username = username,
                nickname = nickname,
                password = password
            )
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }

    override suspend fun updateUserData(
        username: String,
        nickname: String?,
        password: String?,
        newPassword: String?
    ): Response<AuthEntity, Error> {
        return try {
            val response = remote.updateUserData(
                username = username,
                nickname = nickname,
                password = password,
                newPassword = newPassword
            )
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }
}