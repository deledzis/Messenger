package com.deledzis.messenger.data.source.messages

import javax.inject.Inject

/**
 * Create an instance of a [MessagesDataStore]
 */
class MessagesDataStoreFactory @Inject constructor(private val remoteDataStore: MessagesRemoteDataStore) {

    /**
     * Return an instance of the Data Store
     */
    fun retrieveDataStore(): MessagesDataStore = remoteDataStore

}