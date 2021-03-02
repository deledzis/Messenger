package com.deledzis.messenger.data.source.users

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity

/**
 * Interface defining methods for the data operations related to [UserEntity].
 * This is to be implemented by external data source layers, setting the requirements for the
 * operations that need to be implemented
 */
interface UsersDataStore {

    suspend fun getUser(id: Int): Response<UserEntity, Error>

    suspend fun getUsers(search: String?): Response<UsersEntity, Error>

}