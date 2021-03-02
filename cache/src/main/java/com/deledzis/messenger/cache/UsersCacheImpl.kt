package com.deledzis.messenger.cache

import com.deledzis.messenger.cache.db.dao.UsersDao
import com.deledzis.messenger.cache.db.mapper.UserEntityMapper
import com.deledzis.messenger.cache.preferences.UsersLastCacheTime
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.repository.CrudCache

/**
 * Cached implementation for retrieving and saving Push instances. This class implements the
 * [UserEntity] from the Data layer as it is that layers responsibility for defining the
 * operations in which data store implementation layers can carry out.
 */
class UsersCacheImpl(
    private val itemMapper: UserEntityMapper,
    private val lastCacheTime: UsersLastCacheTime,
    private val dao: UsersDao
) : CrudCache<UserEntity, String?> {

    override suspend fun get(id: Int): UserEntity? = dao.get(id)?.let {
        itemMapper.mapFromCached(it)
    }

    override suspend fun getAll(id: Int?): List<UserEntity> = dao.getAll().map {
        itemMapper.mapFromCached(it)
    }

    override suspend fun search(search: String?): List<UserEntity> = dao.search(search ?: "").map {
        itemMapper.mapFromCached(it)
    }

    override suspend fun insert(item: UserEntity): Long =
        dao.insert(item = itemMapper.mapToCached(item))

    override suspend fun insertAll(items: List<UserEntity>): List<Long> =
        dao.insertAll(items = items.map { itemMapper.mapToCached(it) })

    override suspend fun update(item: UserEntity): Int =
        dao.update(item = itemMapper.mapToCached(item))

    override suspend fun delete(id: Int): Int = dao.delete(id = id)

    override suspend fun deleteAll(): Int = dao.deleteAll()

    override suspend fun isCached(id: Int?): Boolean {
        val cachedPushes = dao.getAll()
        return cachedPushes.isNotEmpty()
    }

    /**
     * Set a point in time at when the cache was last updated
     */
    override suspend fun setLastCacheTime(lastCache: Long) {
        lastCacheTime.lastCacheTime = lastCache
    }

    /**
     * Check whether the current cached data exceeds the defined [EXPIRATION_TIME] time
     */
    override suspend fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = this.getLastCacheUpdateTimeMillis()
        return currentTime - lastUpdateTime > EXPIRATION_TIME
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private fun getLastCacheUpdateTimeMillis(): Long {
        return lastCacheTime.lastCacheTime ?: 0L
    }

    companion object {
        private const val EXPIRATION_TIME = (1000 * 60 * 60 * 24).toLong() // 24 hours
    }
}