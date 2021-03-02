package com.deledzis.messenger.data.source.users

import javax.inject.Inject

/**
 * Create an instance of a [UsersRemoteDataStore]
 */
class UsersDataStoreFactory @Inject constructor(private val remoteDataStore: UsersRemoteDataStore) {

    /**
     * Return an instance of the Remote Data Store
     */
    fun retrieveDataStore(): UsersDataStore = remoteDataStore

}