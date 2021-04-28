package com.deledzis.messenger.data.mapper.auth

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals


class AuthMapperTest {
    val authMapper = AuthMapper()

    @Test
    fun authToAuthEntityTest() {
        val authEntity = authMapper.mapToEntity(AuthTestData.auth)
        assertEquals(authEntity.id, AuthTestData.auth.id)
        assertEquals(authEntity.username, AuthTestData.auth.username)
        assertEquals(authEntity.nickname, AuthTestData.auth.nickname)
        assertEquals(authEntity.accessToken, AuthTestData.auth.accessToken)
    }

    @Test
    fun authEntityToAuthTest() {
        val auth = authMapper.mapFromEntity(AuthTestData.authEntity)
        assertEquals(auth.id, AuthTestData.authEntity.id)
        assertEquals(auth.username, AuthTestData.authEntity.username)
        assertEquals(auth.nickname, AuthTestData.authEntity.nickname)
        assertEquals(auth.accessToken, AuthTestData.authEntity.accessToken)
    }
}