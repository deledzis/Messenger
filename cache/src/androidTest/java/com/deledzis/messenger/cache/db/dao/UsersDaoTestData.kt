package com.deledzis.messenger.cache.db.dao

import com.deledzis.messenger.cache.db.model.CachedUser
import com.deledzis.messenger.data.model.users.UserEntity

class UsersDaoTestData {
    companion object {
        val user = CachedUser(
            id = 1,
            username = "Vitaliy",
            nickname = "Vitya"
        )

        val users = listOf(
            CachedUser(
                id = 1,
                username = "Igor",
                nickname = "igorek"
            ),
            CachedUser(
                id = 2,
                username = "Vova",
                nickname = "Vovan"
            )
        )
    }
}