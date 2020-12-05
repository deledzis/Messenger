package com.deledzis.messenger.data.model.chats

import com.deledzis.messenger.data.model.user.User

data class Message(
    val id: Int,
    val type: Boolean,
    val content: String?,
    val date: String,
    val chatId: Int,
    val author: User
)