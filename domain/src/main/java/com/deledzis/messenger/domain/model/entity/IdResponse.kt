package com.deledzis.messenger.domain.model.entity

import java.io.Serializable

data class IdResponse(
    val id: Int?,
    val errorCode: Int,
    val message: String?
) : Serializable