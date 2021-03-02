package com.deledzis.messenger.data.repository

interface CrudCache<T> {
    /**
     * Retrieve a single instance of [T] by [id] from the cache
     */
    suspend fun get(id: Int): T?

    /**
     * Retrieve a list of [T] by [id] from the cache
     */
    suspend fun getAll(id: Int?): List<T>?

    /**
     * Save an instance of [T] to the cache
     */
    suspend fun insert(item: T): Long

    /**
     * Save a given list of [T] to the cache
     */
    suspend fun insertAll(items: List<T>): List<Long>

    /**
     * Update an existing instance of [T] in cache.
     */
    suspend fun update(item: T): Int

    /**
     * Delete an existing instance of [T] from cache.
     */
    suspend fun delete(id: Int): Int

    /**
     * Clear all [T] from the cache
     */
    suspend fun deleteAll(): Int

    /**
     * Checks if cache is not empty.
     * @return true if cache is not empty, otherwise false.
     */
    suspend fun isCached(id: Int? = null): Boolean

    /**
     * Sets last cache time
     */
    suspend fun setLastCacheTime(lastCache: Long)

    /**
     * Checks if the cache is expired.

     * @return true, the cache is expired, otherwise false.
     */
    suspend fun isExpired(): Boolean
}