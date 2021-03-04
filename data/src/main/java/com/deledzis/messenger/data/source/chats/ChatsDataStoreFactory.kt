package com.deledzis.messenger.data.source.chats

import javax.inject.Inject

/**
 * Create an instance of a [ChatsDataStore]
 */
class ChatsDataStoreFactory @Inject constructor(private val remoteDataStore: ChatsRemoteDataStore) {

    /**
     * Return an instance of the Remote Data Store
     */
    fun retrieveDataStore(): ChatsDataStore = remoteDataStore

}