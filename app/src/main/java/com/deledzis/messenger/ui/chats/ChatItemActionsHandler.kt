package com.deledzis.messenger.ui.chats

import com.deledzis.messenger.data.model.chats.ChatReduced

interface ChatItemActionsHandler {
    fun onSelected(chat: ChatReduced)
}