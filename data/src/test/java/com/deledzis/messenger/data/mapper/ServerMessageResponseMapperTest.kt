package com.deledzis.messenger.data.mapper

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ServerMessageResponseMapperTest {
    val serverMessageResponseMapper = ServerMessageResponseMapper()

    @Test
    fun userToUserEntityTest() {
        val serverMessageResponseEntity = serverMessageResponseMapper.mapToEntity(MappersTestData.serverMessageResponse)
        Assertions.assertEquals(serverMessageResponseEntity.errorCode, MappersTestData.serverMessageResponse.errorCode)
        Assertions.assertEquals(serverMessageResponseEntity.message, MappersTestData.serverMessageResponse.message)

    }

    @Test
    fun userEntityToUserTest() {
        val serverMessageResponse = serverMessageResponseMapper.mapFromEntity(MappersTestData.serverMessageResponseEntity)
        Assertions.assertEquals(serverMessageResponse.errorCode, MappersTestData.serverMessageResponseEntity.errorCode)
        Assertions.assertEquals(serverMessageResponse.message, MappersTestData.serverMessageResponseEntity.message)
    }
}