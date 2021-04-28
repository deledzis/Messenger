package com.deledzis.messenger.domain.model.response.messages

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse

data class DeleteMessageResponse(val response: ServerMessageResponse) : Entity()