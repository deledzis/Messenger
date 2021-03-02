package com.deledzis.messenger.data.mapper.users

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.domain.model.entity.user.Users
import javax.inject.Inject

class UsersMapper @Inject constructor(
    private val entityMapper: UserMapper
) : Mapper<UsersEntity, Users> {
    override fun mapFromEntity(type: UsersEntity): Users {
        return Users(
            items = type.items?.map { entityMapper.mapFromEntity(it) },
            errorCode = type.errorCode,
            message = type.message
        )
    }

    override fun mapToEntity(type: Users): UsersEntity {
        return UsersEntity(
            items = type.items?.map { entityMapper.mapToEntity(it) },
            errorCode = 0,
            message = null
        )
    }

}