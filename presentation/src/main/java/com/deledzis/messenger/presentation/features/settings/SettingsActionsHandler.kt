package com.deledzis.messenger.presentation.features.settings

import android.view.View

interface SettingsActionsHandler {
    fun onCancelClicked(view: View)

    fun onLogoutClicked(view: View)
}