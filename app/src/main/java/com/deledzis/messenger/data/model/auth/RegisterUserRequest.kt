package com.deledzis.messenger.data.model.auth

data class RegisterUserRequest(
    val username: String,
    val nickname: String?,
    val password: String
)