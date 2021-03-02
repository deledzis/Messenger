package com.deledzis.messenger.data.mapper

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse
import javax.inject.Inject

class ServerMessageResponseMapper @Inject constructor() :
    Mapper<ServerMessageResponseEntity, ServerMessageResponse> {
    override fun mapFromEntity(type: ServerMessageResponseEntity): ServerMessageResponse {
        return ServerMessageResponse(
            errorCode = type.errorCode,
            message = type.message
        )
    }

    override fun mapToEntity(type: ServerMessageResponse): ServerMessageResponseEntity {
        return ServerMessageResponseEntity(
            errorCode = type.errorCode,
            message = type.message
        )
    }

}