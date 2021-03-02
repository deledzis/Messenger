package com.deledzis.messenger.data.model.auth

class AuthEntity(
    val id: Int,
    val username: String,
    val nickname: String,
    val accessToken: String?,
    val errorCode: Int?,
    val message: String?
)