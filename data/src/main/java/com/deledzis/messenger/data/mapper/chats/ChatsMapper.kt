package com.deledzis.messenger.data.mapper.chats

import com.deledzis.messenger.data.mapper.Mapper
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.domain.model.entity.chats.Chats
import javax.inject.Inject

class ChatsMapper @Inject constructor(
    private val entityMapper: ChatMapper
) : Mapper<ChatsEntity, Chats> {
    override fun mapFromEntity(type: ChatsEntity): Chats {
        return Chats(
            items = type.items?.map { entityMapper.mapFromEntity(it) },
            errorCode = type.errorCode,
            message = type.message
        )
    }

    override fun mapToEntity(type: Chats): ChatsEntity {
        return ChatsEntity(
            items = type.items?.map { entityMapper.mapToEntity(it) },
            errorCode = 0,
            message = type.message
        )
    }
}