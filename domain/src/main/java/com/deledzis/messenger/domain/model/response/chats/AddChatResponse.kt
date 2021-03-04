package com.deledzis.messenger.domain.model.response.chats

import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.chats.Chat

data class AddChatResponse(val response: Chat) : Entity() {
//    override fun isNotEmptyResponse(): Boolean = response.errorCode == 0
}