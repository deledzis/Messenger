package com.deledzis.messenger.domain.model.request.auth

class RegisterRequest(
    val username: String,
    val nickname: String?,
    val password: String
)