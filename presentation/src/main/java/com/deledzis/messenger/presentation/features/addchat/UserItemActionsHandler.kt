package com.deledzis.messenger.presentation.features.addchat

import com.deledzis.messenger.domain.model.entity.user.User

interface UserItemActionsHandler {
    fun onSelected(user: User)
}