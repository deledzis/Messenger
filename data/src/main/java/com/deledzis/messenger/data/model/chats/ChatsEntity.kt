package com.deledzis.messenger.data.model.chats

import com.google.gson.annotations.SerializedName

data class ChatsEntity(
    @SerializedName("chats")
    val items: List<ChatEntity>?,
    val errorCode: Int?,
    val message: String?
)