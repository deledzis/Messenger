package com.deledzis.messenger.room.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Query(value = "SELECT * FROM users_table WHERE id = :userId")
    fun getUser(userId: Int): UserEntity

    @Query("DELETE FROM users_table WHERE id = :id")
    fun delete(id: Int): Int
}