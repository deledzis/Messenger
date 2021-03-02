package com.deledzis.messenger.presentation.screens.settings

import android.view.View

interface SettingsActionsHandler {
    fun onCancelClicked(view: View)

    fun onLogoutClicked(view: View)
}