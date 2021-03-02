package com.deledzis.messenger.data.model.messages

import com.google.gson.annotations.SerializedName

data class MessagesEntity(
    @SerializedName("messages")
    val items: List<MessageEntity>?,
    val errorCode: Int?,
    val message: String?
)