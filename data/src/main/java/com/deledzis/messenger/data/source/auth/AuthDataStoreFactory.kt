package com.deledzis.messenger.data.source.auth

import javax.inject.Inject

/**
 * Create an instance of a [AuthRemoteDataStore]
 */
class AuthDataStoreFactory @Inject constructor(private val remoteDataStore: AuthRemoteDataStore) {

    /**
     * Return an instance of the Remote Data Store
     */
    fun retrieveDataStore(): AuthDataStore = remoteDataStore

}