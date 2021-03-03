package com.deledzis.messenger.domain.model.entity.auth

import java.io.Serializable

data class Auth(
    val id: Int,
    val username: String,
    val nickname: String,
    val accessToken: String
) : Serializable