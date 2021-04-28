package com.deledzis.messenger.data.mapper.users

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserMapperTest {
    val userMapper = UserMapper()

    @Test
    fun userToUserEntityTest() {
        val userEntity = userMapper.mapToEntity(UsersTestData.user)
        assertEquals(userEntity.id, UsersTestData.user.id)
        assertEquals(userEntity.nickname, UsersTestData.user.nickname)
        assertEquals(userEntity.username, UsersTestData.user.username)
    }

    @Test
    fun userEntityToUserTest() {
        val user = userMapper.mapFromEntity(UsersTestData.userEntity)
        assertEquals(user.id, UsersTestData.userEntity.id)
        assertEquals(user.username, UsersTestData.userEntity.username)
        assertEquals(user.nickname, UsersTestData.userEntity.nickname)
    }
}