package com.deledzis.messenger.cache.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.deledzis.messenger.cache.db.model.CachedUser

@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): CachedUser?

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<CachedUser>

    @Query("SELECT * FROM users WHERE username LIKE :search OR nickname LIKE :search")
    suspend fun search(search: String): List<CachedUser>

    @Insert
    suspend fun insert(item: CachedUser): Long

    @Insert
    suspend fun insertAll(items: List<CachedUser>): List<Long>

    @Update
    suspend fun update(item: CachedUser): Int

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun delete(id: Int): Int

    @Query("DELETE FROM users")
    suspend fun deleteAll(): Int
}