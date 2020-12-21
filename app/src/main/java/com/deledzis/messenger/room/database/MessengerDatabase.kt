package com.deledzis.messenger.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deledzis.messenger.room.model.*

@Database(
    entities = [
        UserEntity::class,
        ChatEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MessengerDatabase : RoomDatabase() {

    abstract val userEntityDao: UserEntityDao
    abstract val messageEntityDao: MessageEntityDao
    abstract val chatEntityDao: ChatEntityDao

    companion object {

        @Volatile
        private var INSTANCE: MessengerDatabase? = null

        fun getInstance(context: Context): MessengerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MessengerDatabase::class.java,
                        "messenger_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}