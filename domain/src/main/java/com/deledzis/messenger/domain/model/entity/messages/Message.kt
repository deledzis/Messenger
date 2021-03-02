package com.deledzis.messenger.domain.model.entity.messages

import java.io.Serializable

data class Message(
    val id: Int,
    val type: Int,
    val content: String?,
    val date: String,
    val chatId: Int,
    val authorId: Int,
    val authorName: String?
) : Serializable {
    companion object {
        const val TYPE_TEXT: Int = 0
        const val TYPE_IMAGE: Int = 1
        const val TYPE_FILE: Int = 2
    }
}