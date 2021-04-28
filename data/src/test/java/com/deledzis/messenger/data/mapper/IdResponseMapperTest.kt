package com.deledzis.messenger.data.mapper

import com.deledzis.messenger.data.mapper.users.UserMapper
import com.deledzis.messenger.data.mapper.users.UsersTestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IdResponseMapperTest {
    val idResponseMapper = IdResponseMapper()

    @Test
    fun userToUserEntityTest() {
        val idResponseEntity = idResponseMapper.mapToEntity(MappersTestData.idResponse)
        assertEquals(idResponseEntity.id, MappersTestData.idResponse.id)
    }

    @Test
    fun userEntityToUserTest() {
        val idResponse = idResponseMapper.mapFromEntity(MappersTestData.idResponseEntity)
        assertEquals(idResponse.id, MappersTestData.idResponseEntity.id)
    }
}