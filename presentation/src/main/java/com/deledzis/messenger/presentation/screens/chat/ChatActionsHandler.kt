package com.deledzis.messenger.presentation.screens.chat

import android.view.View

interface ChatActionsHandler {
    fun onBackClicked(view: View)

    fun onSearchClicked(view: View)

    fun onAttachmentClicked(view: View)

    fun onSendClicked(view: View)
}