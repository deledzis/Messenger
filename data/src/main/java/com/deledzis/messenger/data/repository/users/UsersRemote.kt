package com.deledzis.messenger.data.repository.users

import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity

interface UsersRemote {
    suspend fun getUser(id: Int): UserEntity
    suspend fun getUsers(search: String?): UsersEntity
}