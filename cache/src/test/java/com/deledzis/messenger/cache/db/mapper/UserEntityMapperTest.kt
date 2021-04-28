package com.deledzis.messenger.cache.db.mapper

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserEntityMapperTest {
    private val userMapper = UserEntityMapper()

    @Test
    fun userEntityToCachedUserTest() {
        val cachedUser = userMapper.mapToCached(UserEntityMapperTestData.userEntity)
        Assertions.assertEquals(cachedUser.id, UserEntityMapperTestData.userEntity.id)
        Assertions.assertEquals(cachedUser.nickname, UserEntityMapperTestData.userEntity.nickname)
        Assertions.assertEquals(cachedUser.username, UserEntityMapperTestData.userEntity.username)
    }

    @Test
    fun cachedUserToUserEntityTest() {
        val userEntity = userMapper.mapFromCached(UserEntityMapperTestData.cachedUser)
        Assertions.assertEquals(userEntity.id, UserEntityMapperTestData.cachedUser.id)
        Assertions.assertEquals(userEntity.username, UserEntityMapperTestData.cachedUser.username)
        Assertions.assertEquals(userEntity.nickname, UserEntityMapperTestData.cachedUser.nickname)
    }
}