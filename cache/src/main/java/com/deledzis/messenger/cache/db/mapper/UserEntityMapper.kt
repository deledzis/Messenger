package com.deledzis.messenger.cache.db.mapper

import com.deledzis.messenger.cache.db.model.CachedUser
import com.deledzis.messenger.data.model.users.UserEntity
import javax.inject.Inject

class UserEntityMapper @Inject constructor() : EntityMapper<CachedUser, UserEntity> {

    override fun mapFromCached(type: CachedUser): UserEntity {
        return UserEntity(
            id = type.id,
            username = type.username,
            nickname = type.nickname
        )
    }

    override fun mapToCached(type: UserEntity): CachedUser {
        return CachedUser(
            id = type.id,
            username = type.username,
            nickname = type.nickname
        )
    }

}