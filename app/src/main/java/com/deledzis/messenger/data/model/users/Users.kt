package com.deledzis.messenger.data.model.users

data class Users(
    val code: Int,
    val message: String? = null,
    val users: List<User>?
)