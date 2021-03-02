package com.deledzis.messenger.remote.model.auth

class RegisterUserRequest(
    val username: String,
    val nickname: String?,
    val password: String
)