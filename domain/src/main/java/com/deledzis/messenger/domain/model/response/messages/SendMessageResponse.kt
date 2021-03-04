package com.deledzis.messenger.domain.model.response.messages

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.ServerMessageResponse

data class SendMessageResponse(val response: ServerMessageResponse) : Entity() {
//    override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
}