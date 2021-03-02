package com.deledzis.messenger.domain.model.entity.chats

import java.io.Serializable

data class Chats(
    val errorCode: Int? = null,
    val message: String? = null,
    val items: List<Chat>?
) : Serializable