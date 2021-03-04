package com.deledzis.messenger.domain.model.response.messages

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.messages.Messages

data class GetChatMessagesResponse(val response: Messages) : Entity() {
    /*override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
        && !response.items.isNullOrEmpty()*/
}