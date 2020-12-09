package com.deledzis.messenger.data.model.chats

data class Chats(
    val code: Int? = null,
    val message: String? = null,
    val chats: List<ChatReduced>?
)