package com.deledzis.messenger.data.source.users

import com.deledzis.messenger.domain.model.BaseNetworkManager
import javax.inject.Inject

/**
 * Create an instance of a [UsersRemoteDataStore]
 */
class UsersDataStoreFactory @Inject constructor(
    private val cacheDataStore: UsersCacheDataStore,
    private val remoteDataStore: UsersRemoteDataStore,
    private val networkManager: BaseNetworkManager
) {

    /**
     * Returns a DataStore based on whether or not there is content in the cache and the cache
     * has not expired
     */
    fun retrieveDataStore(): UsersDataStore = if (!networkManager.isConnectedToInternet()) {
        retrieveCacheDataStore()
    } else {
        retrieveRemoteDataStore()
    }

    /**
     * Return an instance of the Cache Data Store
     */
    internal fun retrieveCacheDataStore(): UsersDataStore {
        return cacheDataStore
    }

    /**
     * Return an instance of the Remote Data Store
     */
    internal fun retrieveRemoteDataStore(): UsersDataStore {
        return remoteDataStore
    }

}