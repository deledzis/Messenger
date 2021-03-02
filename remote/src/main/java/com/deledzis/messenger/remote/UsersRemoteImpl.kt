package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.data.repository.users.UsersRemote

class UsersRemoteImpl(private val apiService: ApiService) : UsersRemote {

    override suspend fun getUser(id: Int): UserEntity = apiService.getUser(id = id)

    override suspend fun getUsers(search: String?): UsersEntity =
        apiService.getUsersAvailable(search = search)
}