package com.deledzis.messenger.data.source.users

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.data.repository.users.UsersRemote
import javax.inject.Inject

/**
 * Implementation of the [UsersDataStore] interface to provide a means of communicating
 * with the remote data source
 */
open class UsersRemoteDataStore @Inject constructor(private val remote: UsersRemote) :
    UsersDataStore {

    override suspend fun getUser(id: Int): Response<UserEntity, Error> {
        return try {
            val response = remote.getUser(id = id)
            if (response.errorCode == 0) {
                Response.Success(successData = response)
            } else {
                Response.Failure(
                    Error.ResponseError(
                        errorCode = response.errorCode,
                        errorMessage = response.message
                    )
                )
            }
        } catch (e: Exception) {
            Response.Failure(Error.NetworkError(exception = e))
        }
    }

    override suspend fun getUsers(search: String?): Response<UsersEntity, Error> {
        return try {
            val response = remote.getUsers(search = search)
            if (response.errorCode == 0) {
                Response.Success(successData = response)
            } else {
                Response.Failure(
                    Error.ResponseError(
                        errorCode = response.errorCode,
                        errorMessage = response.message
                    )
                )
            }
        } catch (e: Exception) {
            Response.Failure(Error.NetworkError(exception = e))
        }
    }

}