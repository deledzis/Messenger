package com.deledzis.messenger.data.model.users

data class Users(
    val code: Int? = null,
    val message: String? = null,
    val users: List<User>?
)