package com.deledzis.messenger.data.model.messages

import java.io.Serializable

data class MessageEntity(
    val id: Int,
    val type: Int,
    val content: String?,
    val date: String,
    val chatId: Int,
    val authorId: Int,
    val authorName: String?
) : Serializable