package com.deledzis.messenger.domain.model.entity.user

import java.io.Serializable

data class Users(
    val errorCode: Int? = null,
    val message: String? = null,
    val items: List<User>?
) : Serializable