package com.deledzis.messenger.cache.mapper

import com.deledzis.messenger.cache.preferences.user.AuthenticatedUser
import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.User
import com.deledzis.messenger.domain.model.entity.user.Users

class UserTestData {
    companion object {
        val auth = Auth(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
            accessToken = "123"
        )

        val authedUser = AuthenticatedUser(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
            accessToken = "123"
        )

    }
}