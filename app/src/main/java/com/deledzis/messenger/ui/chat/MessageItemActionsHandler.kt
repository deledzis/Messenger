package com.deledzis.messenger.ui.chat

import com.deledzis.messenger.data.model.chats.Message

interface MessageItemActionsHandler {
    fun onSelected(message: Message)
}