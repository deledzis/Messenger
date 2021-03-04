package com.deledzis.messenger.data.source.users

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.data.repository.users.UsersRemote
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Implementation of the [UsersDataStore] interface to provide a means of communicating
 * with the remote data source
 */
class UsersRemoteDataStore @Inject constructor(private val remote: UsersRemote) :
    UsersDataStore {

    override suspend fun getUser(id: Int): Response<UserEntity, Error> {
        return try {
            val response = remote.getUser(id = id)
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }

    override suspend fun getUsers(search: String?): Response<UsersEntity, Error> {
        return try {
            val response = remote.getUsers(search = search)
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }

    override suspend fun clearUsers(): Response<Int, Error> {
        return Response.Failure(Error.UnsupportedOperationError())
    }

    override suspend fun saveUsers(items: List<UserEntity>): Response<Int, Error> {
        return Response.Failure(Error.UnsupportedOperationError())
    }

}