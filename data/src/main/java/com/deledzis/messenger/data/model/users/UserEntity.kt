package com.deledzis.messenger.data.model.users

data class UserEntity(
    val id: Int?,
    val username: String,
    val nickname: String?,
    val errorCode: Int?,
    val message: String?
)