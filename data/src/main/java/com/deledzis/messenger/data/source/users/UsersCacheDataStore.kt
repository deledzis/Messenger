package com.deledzis.messenger.data.source.users

import com.deledzis.messenger.common.usecase.EmptyCacheException
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.data.repository.CrudCache
import javax.inject.Inject

/**
 * Implementation of the [UsersDataStore] interface to provide a means of communicating
 * with the local data source
 */
open class UsersCacheDataStore @Inject constructor(
    private val cache: CrudCache<UserEntity, String?>
) : UsersDataStore {

    override suspend fun getUsers(search: String?): Response<UsersEntity, Error> {
        return try {
            val response = cache.search(search = search)
            if (response != null) {
                Response.Success(
                    successData = UsersEntity(
                        items = response
                    )
                )
            } else {
                Response.Failure(Error.MissingInCacheError())
            }
        } catch (e: Exception) {
            Response.Failure(Error.PersistenceError(exception = e))
        }
    }

    override suspend fun getUser(id: Int): Response<UserEntity, Error> {
        return try {
            val response = cache.get(id = id)
            if (response != null) {
                Response.Success(successData = response)
            } else {
                Response.Failure(Error.PersistenceError(exception = EmptyCacheException()))
            }
        } catch (e: Exception) {
            Response.Failure(Error.PersistenceError(exception = e))
        }
    }

    override suspend fun clearUsers(): Response<Int, Error> {
        return try {
            val response = cache.deleteAll()
            Response.Success(successData = response)
        } catch (e: Exception) {
            Response.Failure(Error.PersistenceError(exception = e))
        }
    }

    override suspend fun saveUsers(items: List<UserEntity>): Response<Int, Error> {
        return try {
            val response = cache.insertAll(items = items)
            cache.setLastCacheTime(System.currentTimeMillis())
            Response.Success(successData = response.size)
        } catch (e: Exception) {
            Response.Failure(Error.PersistenceError(exception = e))
        }
    }

}