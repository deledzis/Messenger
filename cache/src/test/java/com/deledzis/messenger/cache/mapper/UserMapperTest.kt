package com.deledzis.messenger.cache.mapper

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserMapperTest {
    val userMapper = UserMapper()

    @Test
    fun userToUserEntityTest() {
        val authenticatedUser = userMapper.mapToEntity(UserTestData.auth)
        Assertions.assertEquals(authenticatedUser.id, UserTestData.auth.id)
        Assertions.assertEquals(authenticatedUser.nickname, UserTestData.auth.nickname)
        Assertions.assertEquals(authenticatedUser.username, UserTestData.auth.username)
        Assertions.assertEquals(authenticatedUser.accessToken, UserTestData.auth.accessToken)
    }

    @Test
    fun userEntityToUserTest() {
        val auth = userMapper.mapFromEntity(UserTestData.authedUser)
        Assertions.assertEquals(auth.id, UserTestData.authedUser.id)
        Assertions.assertEquals(auth.username, UserTestData.authedUser.username)
        Assertions.assertEquals(auth.nickname, UserTestData.authedUser.nickname)
        Assertions.assertEquals(auth.accessToken, UserTestData.authedUser.accessToken)
    }
}