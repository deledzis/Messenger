package com.deledzis.messenger.data.mapper.auth

import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.domain.model.entity.auth.Auth

class AuthTestData {
    companion object {
        val auth = Auth(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
            accessToken = "123"
        )

        val authEntity = AuthEntity(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
            accessToken = "123"
        )

    }
}