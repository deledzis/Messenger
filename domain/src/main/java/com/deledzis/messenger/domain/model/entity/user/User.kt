package com.deledzis.messenger.domain.model.entity.user

import java.io.Serializable

data class User(
    val id: Int?,
    val username: String,
    val nickname: String? = null,
    val errorCode: Int?,
    val message: String? = null
) : Serializable