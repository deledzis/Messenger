package com.deledzis.messenger.data.mapper.messages

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.model.messages.MessagesEntity
import com.deledzis.messenger.domain.model.entity.messages.Messages
import javax.inject.Inject

class MessagesMapper @Inject constructor(
    private val entityMapper: MessageMapper
) : Mapper<MessagesEntity, Messages> {
    override fun mapFromEntity(type: MessagesEntity): Messages {
        return Messages(
            items = type.items?.map { entityMapper.mapFromEntity(it) },
            errorCode = type.errorCode,
            message = type.message
        )
    }

    override fun mapToEntity(type: Messages): MessagesEntity {
        return MessagesEntity(
            items = type.items?.map { entityMapper.mapToEntity(it) },
            errorCode = 0,
            message = type.message
        )
    }
}