package com.deledzis.messenger.data.source.messages

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MessagesDataStoreFactoryTest {

    private val factory by lazy { MessagesDataStoreFactory(mockk()) }

    @Test
    fun retrieveDataStore() {
        assertDoesNotThrow {
            val dataStore = factory.retrieveDataStore()
            assert(dataStore is MessagesRemoteDataStore)
        }
    }

}