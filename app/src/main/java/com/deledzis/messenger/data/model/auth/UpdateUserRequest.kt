package com.deledzis.messenger.data.model.auth

data class UpdateUserRequest(
    val userId: Int,
    val username: String,
    val nickname: String?,
    val password: String?,
    val new_password: String?
)