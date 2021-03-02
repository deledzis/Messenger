package com.deledzis.messenger.domain.model.entity.messages

import java.io.Serializable

data class Messages(
    val errorCode: Int? = null,
    val message: String? = null,
    val items: List<Message>?
) : Serializable