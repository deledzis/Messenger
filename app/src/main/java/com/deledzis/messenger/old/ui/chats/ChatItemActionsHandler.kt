package com.deledzis.messenger.old.ui.chats

import com.deledzis.messenger.domain.model.entity.chats.ChatReduced

interface ChatItemActionsHandler {
    fun onSelected(chat: ChatReduced)
}