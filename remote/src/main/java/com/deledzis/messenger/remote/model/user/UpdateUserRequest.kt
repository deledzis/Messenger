package com.deledzis.messenger.remote.model.user

data class UpdateUserRequest(
    val username: String,
    val nickname: String?,
    val password: String?,
    val newPassword: String?
)