package com.deledzis.messenger.domain.model.request.messages

class GetChatMessagesRequest(
    val chatId: Int,
    val search: String?
)