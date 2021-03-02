package com.deledzis.messenger.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.deledzis.messenger.cache.db.dao.UsersDao
import com.deledzis.messenger.cache.db.model.CachedUser
import com.deledzis.messenger.cache.db.util.Converters

@Database(
    entities = [
        CachedUser::class
    ],
    version = 1
)
@TypeConverters(
    value = [
        Converters::class
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}