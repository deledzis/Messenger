package com.deledzis.messenger.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chats_table",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class,  parentColumns = ["id"], childColumns = ["interlocutorId"])
    ]
)
data class ChatEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "interlocutorId")
    val interlocutor: Int
)