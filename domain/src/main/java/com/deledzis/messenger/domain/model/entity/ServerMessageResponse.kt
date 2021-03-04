package com.deledzis.messenger.domain.model.entity

import java.io.Serializable

data class ServerMessageResponse(
    val errorCode: Int,
    val message: String? = null
) : Serializable