package com.deledzis.messenger.domain.model.response.chats

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse

data class DeleteChatResponse(val response: ServerMessageResponse) : Entity()