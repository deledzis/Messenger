package com.deledzis.messenger.data.model.chats

import com.deledzis.messenger.data.model.user.User
import java.time.LocalDateTime

data class Message(
    val id: Int,
    val type: Boolean,
    val content: String?,
    val date: LocalDateTime,
    val chatId: Int,
    val author: User
)