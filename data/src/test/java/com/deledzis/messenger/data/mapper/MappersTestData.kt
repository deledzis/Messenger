package com.deledzis.messenger.data.mapper

import com.deledzis.messenger.data.model.IdResponseEntity
import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.domain.model.entity.IdResponse
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse

class MappersTestData {
    companion object {
        val idResponse = IdResponse(id = 2)
        val idResponseEntity = IdResponseEntity(id = 2)

        val serverMessageResponse = ServerMessageResponse(
            errorCode = 0,
            message = "Looks good"
        )
        val serverMessageResponseEntity = ServerMessageResponseEntity(
            errorCode = 0,
            message = "Looks good"
        )
    }
}