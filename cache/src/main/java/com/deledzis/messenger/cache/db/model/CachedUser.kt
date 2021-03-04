package com.deledzis.messenger.cache.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model used solely for the caching of a LocationPassword
 */
@Entity(tableName = "users")
data class CachedUser(
    @PrimaryKey
    val id: Int?,
    val username: String,
    val nickname: String?,
)