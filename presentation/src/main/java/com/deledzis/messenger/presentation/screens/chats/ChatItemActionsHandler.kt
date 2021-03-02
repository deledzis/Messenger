package com.deledzis.messenger.presentation.screens.chats

import com.deledzis.messenger.domain.model.entity.chats.Chat

interface ChatItemActionsHandler {
    fun onSelected(chat: Chat)
}