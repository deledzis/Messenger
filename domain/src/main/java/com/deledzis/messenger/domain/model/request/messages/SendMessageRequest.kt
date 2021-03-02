package com.deledzis.messenger.domain.model.request.messages

class SendMessageRequest(
    val chatId: Int,
    val authorId: Int,
    val type: Int,
    val content: String
)