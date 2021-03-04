package com.deledzis.messenger.data.model.chats

import com.deledzis.messenger.data.model.messages.MessageEntity

data class ChatEntity(
    val id: Int,
    val title: String,
    val interlocutorId: Int,
    val lastMessage: MessageEntity?
)