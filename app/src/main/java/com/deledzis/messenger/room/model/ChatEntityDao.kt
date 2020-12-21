package com.deledzis.messenger.room.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatEntityDao {
    @Insert
    fun insertChat(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateChats(chats: List<ChatEntity>)

    @Query(value = "SELECT * FROM chats_table")
    fun getAllChats(): List<ChatEntity>

    @Query(value = "SELECT * FROM chats_table WHERE id = :chatId")
    fun getChat(chatId: Int): ChatEntity

    @Query("DELETE FROM chats_table")
    fun deleteAll(): Int
}