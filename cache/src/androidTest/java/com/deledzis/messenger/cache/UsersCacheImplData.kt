package com.deledzis.messenger.cache

import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity

class UsersCacheImplData {
    companion object {
        val user = UserEntity(
            id = 1,
            username = "Vitaliy",
            nickname = "Vitya"
        )

        val users = listOf(
            UserEntity(
                id = 1,
                username = "Igor",
                nickname = "igorek"
            ),
            UserEntity(
                id = 2,
                username = "Vova",
                nickname = "Vovan"
            )
        )
    }
}