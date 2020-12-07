package com.deledzis.messenger.data.model.chats

data class AddChatRequest(
    val authorId: Int,
    val interlocutorId: Int
)