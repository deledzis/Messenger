package com.deledzis.messenger.presentation.features.chat

import com.deledzis.messenger.domain.model.entity.messages.Message

interface MessageItemActionsHandler {

    fun onSelected(message: Message)

    fun onLongClicked(message: Message): Boolean

}