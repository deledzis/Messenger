package com.deledzis.messenger.ui.addchat

import com.deledzis.messenger.data.model.users.User

interface UserItemActionsHandler {
    fun onSelected(user: User)
}