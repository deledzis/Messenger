package com.deledzis.messenger.domain.model.entity.auth

data class Auth(
    val id: Int,
    val username: String,
    val nickname: String?,
    val accessToken: String
) : Serializable