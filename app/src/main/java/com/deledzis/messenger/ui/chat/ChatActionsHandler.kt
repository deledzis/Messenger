package com.deledzis.messenger.ui.chat

import android.view.View

interface ChatActionsHandler {
    fun onBackClicked(view: View)

    fun onSearchClicked(view: View)

    fun onAttachmentClicked(view: View)

    fun onSendClicked(view: View)
}