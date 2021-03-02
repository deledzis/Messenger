package com.deledzis.messenger.old.ui.addchat

import com.deledzis.messenger.old.data.model.users.User

interface UserItemActionsHandler {
    fun onSelected(user: User)
}