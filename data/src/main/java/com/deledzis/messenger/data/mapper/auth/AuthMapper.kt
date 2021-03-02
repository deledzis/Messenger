package com.deledzis.messenger.data.mapper.auth

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import javax.inject.Inject

class AuthMapper @Inject constructor() : Mapper<AuthEntity, Auth> {
    override fun mapFromEntity(type: AuthEntity): Auth {
        return Auth(
            id = type.id,
            username = type.username,
            nickname = type.nickname,
            accessToken = type.accessToken,
            errorCode = type.errorCode,
            message = type.message
        )
    }

    override fun mapToEntity(type: Auth): AuthEntity {
        return AuthEntity(
            id = type.id,
            username = type.username,
            nickname = type.nickname,
            accessToken = type.accessToken,
            errorCode = type.errorCode,
            message = type.message
        )
    }

}