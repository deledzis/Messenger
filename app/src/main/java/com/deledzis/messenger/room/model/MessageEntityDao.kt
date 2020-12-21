package com.deledzis.messenger.room.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageEntityDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<MessageEntity>)

    @Query(value = "SELECT * FROM messages_table WHERE chatId = :chatId ")
    fun getChatMessages(chatId: Int): List<MessageEntity>

    @Query(value = "SELECT * FROM messages_table WHERE chatId = :chatId ORDER BY id DESC LIMIT 1")
    fun getLastMessage(chatId: Int): MessageEntity
}