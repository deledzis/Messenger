package com.deledzis.messenger.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.db.Database
import com.deledzis.messenger.cache.db.dao.UsersDao
import com.deledzis.messenger.cache.db.mapper.UserEntityMapper
import com.deledzis.messenger.cache.preferences.UsersLastCacheTime
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class UsersCacheImplTest {

    companion object {
        private const val PREF_APP_PACKAGE_NAME_TEST = "com.deledzis.messenger.preferencestest"
        private const val EXPIRATION_TIME = (1000 * 60 * 60 * 24).toLong() // 24 hours
    }

    lateinit var sharedPreferences: SharedPreferences
    lateinit var context: Context
    lateinit var database: Database
    lateinit var usersDao: UsersDao
    lateinit var usersCacheImpl: UsersCacheImpl

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        sharedPreferences = context.getSharedPreferences(
            PREF_APP_PACKAGE_NAME_TEST, Context.MODE_PRIVATE
        )
        database = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java,
        )
            .fallbackToDestructiveMigration()
            .build()
        usersDao = database.usersDao()
        usersCacheImpl = UsersCacheImpl(
            UserEntityMapper(), UsersLastCacheTime(sharedPreferences), usersDao
        )

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun deleteAllTest() {
        runBlocking {
            usersCacheImpl.insertAll(UsersCacheImplData.users)
            usersCacheImpl.deleteAll()
            val users = usersCacheImpl.getAll(null)
            assertEquals(0, users.size)
        }

    }

    @Test
    fun deleteTest() {
        runBlocking {
            usersCacheImpl.insertAll(UsersCacheImplData.users)
            usersCacheImpl.delete(UsersCacheImplData.users[0].id!!)
            val users = usersCacheImpl.getAll(null)
            assertNotEquals(UsersCacheImplData.users.size, users.size)
            assertEquals(users[0].id, UsersCacheImplData.users[1].id)
            assertEquals(users[0].username, UsersCacheImplData.users[1].username)
            assertEquals(users[0].nickname, UsersCacheImplData.users[1].nickname)
            usersCacheImpl.deleteAll()
        }
    }

    @Test
    fun insertGetTest() {
        runBlocking {
            usersCacheImpl.deleteAll()
            usersCacheImpl.insert(UsersCacheImplData.user)
            val insertedUser = usersCacheImpl.get(UsersCacheImplData.user.id!!)
            assertEquals(insertedUser?.id, UsersCacheImplData.user.id)
            assertEquals(insertedUser?.username, UsersCacheImplData.user.username)
            assertEquals(insertedUser?.nickname, UsersCacheImplData.user.nickname)
            usersCacheImpl.deleteAll()
        }
    }

    @Test
    fun insertAllGetAllTest() {
        runBlocking {
            usersCacheImpl.deleteAll()
            usersCacheImpl.insertAll(UsersCacheImplData.users)
            val insertedUsers = usersCacheImpl.getAll(null)
            assertEquals(insertedUsers.size, UsersCacheImplData.users.size)
            assertEquals(insertedUsers[0].id, UsersCacheImplData.users[0].id)
            assertEquals(insertedUsers[0].username, UsersCacheImplData.users[0].username)
            assertEquals(insertedUsers[0].nickname, UsersCacheImplData.users[0].nickname)
            assertEquals(insertedUsers[1].id, UsersCacheImplData.users[1].id)
            assertEquals(insertedUsers[1].username, UsersCacheImplData.users[1].username)
            assertEquals(insertedUsers[1].nickname, UsersCacheImplData.users[1].nickname)
            usersCacheImpl.deleteAll()
        }
    }

    @Test
    fun searchTest() {
        runBlocking {
            usersCacheImpl.insertAll(UsersCacheImplData.users)
            val foundUsers = usersCacheImpl.search(UsersCacheImplData.users[1].nickname)
            assertEquals(foundUsers[0].id, UsersCacheImplData.users[1].id)
            assertEquals(foundUsers[0].username, UsersCacheImplData.users[1].username)
            assertEquals(foundUsers[0].nickname, UsersCacheImplData.users[1].nickname)
            usersCacheImpl.deleteAll()
        }
    }

    @Test
    fun updateTest() {
        runBlocking {
            usersCacheImpl.insertAll(UsersCacheImplData.users)
            usersCacheImpl.update(UsersCacheImplData.user)
            val updatedUser = usersCacheImpl.get(UsersCacheImplData.user.id!!)
            assertEquals(updatedUser?.id, UsersCacheImplData.user.id)
            assertEquals(updatedUser?.username, UsersCacheImplData.user.username)
            assertEquals(updatedUser?.nickname, UsersCacheImplData.user.nickname)
            usersCacheImpl.deleteAll()
        }
    }

    @Test
    fun isCachedTest(){
        runBlocking {
            usersCacheImpl.deleteAll()
            assertFalse(usersCacheImpl.isCached())
            usersCacheImpl.insert(UsersCacheImplData.user)
            assertTrue(usersCacheImpl.isCached())
            usersCacheImpl.deleteAll()
        }
    }

    @Test
    fun setIsExpiredTest() {
        runBlocking {
            usersCacheImpl.setLastCacheTime(System.currentTimeMillis())
            assertFalse(usersCacheImpl.isExpired())
            usersCacheImpl.setLastCacheTime(System.currentTimeMillis() - EXPIRATION_TIME - 10L)
            assertTrue(usersCacheImpl.isExpired())
        }
    }
}