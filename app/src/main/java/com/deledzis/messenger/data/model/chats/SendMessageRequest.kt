package com.deledzis.messenger.data.model.chats

data class SendMessageRequest(
    val chatId: Int,
    val authorId: Int,
    val type: Boolean,
    val content: String
)