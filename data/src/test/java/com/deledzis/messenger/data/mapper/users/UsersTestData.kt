package com.deledzis.messenger.data.mapper.users

import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.domain.model.entity.user.User
import com.deledzis.messenger.domain.model.entity.user.Users

class UsersTestData {
    companion object {
        val userEntity = UserEntity(
            id = 1,
            username = "Igor",
            nickname = "igorek"
        )

        val user = User(
            id = 1,
            username = "Igor",
            nickname = "igorek"
        )

        val usersEntity = UsersEntity(
            items = listOf(
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
        )

        val users = Users(
            items = listOf(
                User(
                    id = 1,
                    username = "Igor",
                    nickname = "igorek"
                ),
                User(
                    id = 2,
                    username = "Vova",
                    nickname = "Vovan"
                )
            )
        )
    }
}