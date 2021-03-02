package com.deledzis.messenger.data.model.users

import com.google.gson.annotations.SerializedName

data class UsersEntity(
    @SerializedName("users")
    val items: List<UserEntity>?,
    val errorCode: Int?,
    val message: String?
)