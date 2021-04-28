package com.deledzis.messenger.data.mapper.users

import com.deledzis.messenger.data.mapper.messages.MessagesTestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UsersMapperTest {
    val usersMapper = UsersMapper(UserMapper())

    @Test
    fun messagesToMessagesEntityTest() {
        val usersEntity = usersMapper.mapToEntity(UsersTestData.users)
        assertEquals(usersEntity.items.size, UsersTestData.users.items.size)
        usersEntity.items.zip(UsersTestData.users.items).forEach{ pair ->
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(pair.first.username, pair.second.username)
            assertEquals(pair.first.nickname, pair.second.nickname)
        }
    }

    @Test
    fun messagesEntityToMessagesTest() {
        val users = usersMapper.mapFromEntity(UsersTestData.usersEntity)
        assertEquals(users.items.size, UsersTestData.usersEntity.items.size)
        users.items.zip(UsersTestData.usersEntity.items).forEach{ pair ->
            assertEquals(pair.first.id, pair.second.id)
            assertEquals(pair.first.username, pair.second.username)
            assertEquals(pair.first.nickname, pair.second.nickname)
        }
    }
}