package com.deledzis.messenger.domain.model.request.auth

class UpdateUserDataRequest(
    val username: String,
    val nickname: String?,
    val password: String?,
    val newPassword: String?
)