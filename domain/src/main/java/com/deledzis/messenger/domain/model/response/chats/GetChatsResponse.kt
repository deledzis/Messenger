package com.deledzis.messenger.domain.model.response.chats

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.chats.Chats

data class GetChatsResponse(val response: Chats) : Entity()