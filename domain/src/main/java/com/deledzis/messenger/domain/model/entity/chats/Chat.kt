package com.deledzis.messenger.domain.model.entity.chats

import com.deledzis.messenger.domain.model.entity.messages.Message
import java.io.Serializable

data class Chat(
    val id: Int,
    val title: String,
    val interlocutorId: Int,
    val lastMessage: Message,
    val errorCode: Int?,
    val message: String?
) : Serializable