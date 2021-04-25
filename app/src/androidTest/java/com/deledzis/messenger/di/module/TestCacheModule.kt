package com.deledzis.messenger.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.deledzis.messenger.cache.UsersCacheImpl
import com.deledzis.messenger.cache.db.Database
import com.deledzis.messenger.cache.db.dao.UsersDao
import com.deledzis.messenger.cache.db.mapper.UserEntityMapper
import com.deledzis.messenger.cache.mapper.UserMapper
import com.deledzis.messenger.cache.preferences.UsersLastCacheTime
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.cache.preferences.user.UserStore
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.repository.CrudCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestCacheModule {
    companion object {
        private const val PREF_APP_PACKAGE_NAME = "com.deledzis.messenger.preferences.test"

        @get:Synchronized
        var database: Database? = null
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            PREF_APP_PACKAGE_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): Database {
        synchronized(this) {
            if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    Database::class.java,
                    "spbstu_messenger_test.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
        return database!!
    }

    @Singleton
    @Provides
    fun provideUserDataImpl(
        userStore: UserStore,
        userMapper: UserMapper
    ): UserData {
        return UserData(
            userStore = userStore,
            userMapper = userMapper
        )
    }

    @Singleton
    @Provides
    fun provideUsersDao(database: Database): UsersDao {
        return database.usersDao()
    }

    @Singleton
    @Provides
    fun provideUsersCache(
        itemMapper: UserEntityMapper,
        lastCacheTime: UsersLastCacheTime,
        dao: UsersDao
    ): CrudCache<UserEntity, String?> {
        return UsersCacheImpl(
            itemMapper = itemMapper,
            lastCacheTime = lastCacheTime,
            dao = dao
        )
    }
}
