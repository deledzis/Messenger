package com.deledzis.messenger.data.mapper

import com.deledzis.messenger.data.model.IdResponseEntity
import com.deledzis.messenger.domain.model.entity.IdResponse
import javax.inject.Inject

class IdResponseMapper @Inject constructor() : Mapper<IdResponseEntity, IdResponse> {
    override fun mapFromEntity(type: IdResponseEntity): IdResponse {
        return IdResponse(
            id = type.id,
            errorCode = type.errorCode,
            message = type.message
        )
    }

    override fun mapToEntity(type: IdResponse): IdResponseEntity {
        return IdResponseEntity(
            id = type.id,
            errorCode = type.errorCode,
            message = type.message
        )
    }

}