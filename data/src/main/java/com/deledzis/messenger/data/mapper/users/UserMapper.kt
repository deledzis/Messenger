package com.deledzis.messenger.data.mapper.users

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.domain.model.entity.user.User
import javax.inject.Inject

class UserMapper @Inject constructor() : Mapper<UserEntity, User> {
    override fun mapFromEntity(type: UserEntity): User {
        return User(
            id = type.id,
            username = type.username,
            nickname = type.nickname
        )
    }

    override fun mapToEntity(type: User): UserEntity {
        return UserEntity(
            id = type.id,
            username = type.username,
            nickname = type.nickname
        )
    }

}