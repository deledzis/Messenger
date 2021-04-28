package com.deledzis.messenger.cache.db.mapper

import com.deledzis.messenger.cache.db.model.CachedUser
import com.deledzis.messenger.cache.preferences.user.AuthenticatedUser
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.domain.model.entity.auth.Auth

class UserEntityMapperTestData {
    companion object {
        val userEntity = UserEntity(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
        )

        val cachedUser = CachedUser(
            id = 1,
            username = "Igor",
            nickname = "Igorek",
        )
    }
}