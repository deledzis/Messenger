package com.deledzis.messenger.presentation.features.chats

import android.view.View

interface ChatsActionsHandler {
    fun onSettingsClicked(view: View)

    fun onAddChatClicked(view: View)
}