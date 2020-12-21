package com.deledzis.messenger.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.deledzis.messenger.data.model.users.User

@Entity(
    tableName = "messages_table",
    foreignKeys = [
        ForeignKey(entity = ChatEntity::class, parentColumns = ["id"], childColumns = ["chatId"]),
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["authorId"])
    ]
)
data class MessageEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "type")
    val type: Boolean,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "chatId")
    val chatId: Int,
    @ColumnInfo(name = "authorId")
    val author: Int
)