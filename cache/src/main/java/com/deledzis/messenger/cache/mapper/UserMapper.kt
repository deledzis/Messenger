package com.deledzis.messenger.cache.mapper

import com.deledzis.messenger.cache.preferences.user.AuthenticatedUser
import com.deledzis.messenger.domain.model.entity.auth.Auth
import javax.inject.Inject

class UserMapper @Inject constructor() : Mapper<AuthenticatedUser, Auth> {
    override fun mapFromEntity(type: AuthenticatedUser): Auth {
        return Auth(
            id = type.id,
            username = type.username,
            nickname = type.nickname,
            accessToken = type.accessToken
        )
    }

    override fun mapToEntity(type: Auth): AuthenticatedUser {
        return AuthenticatedUser(
            id = type.id,
            username = type.username,
            nickname = type.nickname,
            accessToken = type.accessToken
        )
    }

}