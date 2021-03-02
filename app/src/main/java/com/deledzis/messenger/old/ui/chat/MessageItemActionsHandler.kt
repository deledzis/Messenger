package com.deledzis.messenger.old.ui.chat

import com.deledzis.messenger.domain.model.entity.messages.Message

interface MessageItemActionsHandler {
    fun onSelected(message: Message)
}