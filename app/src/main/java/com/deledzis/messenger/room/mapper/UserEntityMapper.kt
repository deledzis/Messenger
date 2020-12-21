package com.deledzis.messenger.room.mapper

import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.room.model.UserEntity

class UserEntityMapper: EntityMapper<UserEntity, User> {
    override fun mapFromEntity(type: UserEntity): User {
        return User(type.id, type.username, type.nickname)
    }

    override fun mapToEntity(type: User): UserEntity {
        return UserEntity(type.id, type.username, type.nickname)
    }
}