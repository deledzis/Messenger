package com.deledzis.messenger.presentation.screens.chat

import com.deledzis.messenger.domain.model.entity.messages.Message

interface MessageItemActionsHandler {
    fun onSelected(message: Message)
}