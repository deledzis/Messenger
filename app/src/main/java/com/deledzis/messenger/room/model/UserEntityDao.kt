package com.deledzis.messenger.room.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserEntityDao {
    @Insert
    fun insertUser(user: UserEntity)

    @Query(value = "SELECT * FROM users_table WHERE id = :userId")
    fun getUser(userId: Int): UserEntity
}