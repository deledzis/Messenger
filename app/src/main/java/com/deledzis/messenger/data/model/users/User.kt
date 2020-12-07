package com.deledzis.messenger.data.model.users

data class User(
    val id: Int,
    val username: String,
    val nickname: String? = null
)