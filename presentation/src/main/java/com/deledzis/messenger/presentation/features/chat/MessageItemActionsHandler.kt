package com.deledzis.messenger.presentation.features.chat

import com.deledzis.messenger.domain.model.entity.messages.Message

fun interface MessageItemActionsHandler {
    fun onSelected(message: Message)
}