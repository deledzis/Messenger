package com.deledzis.messenger.data.model.chats

data class Messages(
    val code: Int,
    val message: String? = null,
    val messages: List<Message>?
)