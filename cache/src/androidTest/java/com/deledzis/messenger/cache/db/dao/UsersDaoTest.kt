package com.deledzis.messenger.cache.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.UsersCacheImpl
import com.deledzis.messenger.cache.db.Database
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class UsersDaoTest {

    lateinit var context: Context
    lateinit var database: Database
    lateinit var usersDao: UsersDao

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java,
        )
            .fallbackToDestructiveMigration()
            .build()
        this.usersDao = database.usersDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun deleteAllTest() {
        runBlocking {
            usersDao.insertAll(UsersDaoTestData.users)
            usersDao.deleteAll()
            val users = usersDao.getAll()
            assertEquals(0, users.size)
        }

    }

    @Test
    fun deleteTest() {
        runBlocking {
            usersDao.insertAll(UsersDaoTestData.users)
            usersDao.delete(UsersDaoTestData.users[0].id!!)
            val users = usersDao.getAll()
            Assert.assertNotEquals(UsersDaoTestData.users.size, users.size)
            assertEquals(users[0].id, UsersDaoTestData.users[1].id)
            assertEquals(users[0].username, UsersDaoTestData.users[1].username)
            assertEquals(users[0].nickname, UsersDaoTestData.users[1].nickname)
            usersDao.deleteAll()
        }
    }

    @Test
    fun insertGetTest() {
        runBlocking {
            usersDao.deleteAll()
            usersDao.insert(UsersDaoTestData.user)
            val insertedUser = usersDao.get(UsersDaoTestData.user.id!!)
            assertEquals(insertedUser?.id, UsersDaoTestData.user.id)
            assertEquals(insertedUser?.username, UsersDaoTestData.user.username)
            assertEquals(insertedUser?.nickname, UsersDaoTestData.user.nickname)
            usersDao.deleteAll()
        }
    }

    @Test
    fun insertAllGetAllTest() {
        runBlocking {
            usersDao.deleteAll()
            usersDao.insertAll(UsersDaoTestData.users)
            val insertedUsers = usersDao.getAll()
            assertEquals(insertedUsers.size, UsersDaoTestData.users.size)
            assertEquals(insertedUsers[0].id, UsersDaoTestData.users[0].id)
            assertEquals(insertedUsers[0].username, UsersDaoTestData.users[0].username)
            assertEquals(insertedUsers[0].nickname, UsersDaoTestData.users[0].nickname)
            assertEquals(insertedUsers[1].id, UsersDaoTestData.users[1].id)
            assertEquals(insertedUsers[1].username, UsersDaoTestData.users[1].username)
            assertEquals(insertedUsers[1].nickname, UsersDaoTestData.users[1].nickname)
            usersDao.deleteAll()
        }
    }

    @Test
    fun searchTest() {
        runBlocking {
            usersDao.insertAll(UsersDaoTestData.users)
            val foundUsers = usersDao.search(UsersDaoTestData.users[1].nickname!!)
            assertEquals(foundUsers[0].id, UsersDaoTestData.users[1].id)
            assertEquals(foundUsers[0].username, UsersDaoTestData.users[1].username)
            assertEquals(foundUsers[0].nickname, UsersDaoTestData.users[1].nickname)
            usersDao.deleteAll()
        }
    }

    @Test
    fun updateTest() {
        runBlocking {
            usersDao.insertAll(UsersDaoTestData.users)
            usersDao.update(UsersDaoTestData.user)
            val updatedUser = usersDao.get(UsersDaoTestData.user.id!!)
            assertEquals(updatedUser?.id, UsersDaoTestData.user.id)
            assertEquals(updatedUser?.username, UsersDaoTestData.user.username)
            assertEquals(updatedUser?.nickname, UsersDaoTestData.user.nickname)
            usersDao.deleteAll()
        }
    }
}