package com.deledzis.messenger.data.source.chats

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class ChatsDataStoreFactoryTest {

    private val factory by lazy { ChatsDataStoreFactory(mockk()) }

    @Test
    fun retrieveDataStore() {
        assertDoesNotThrow {
            val dataStore = factory.retrieveDataStore()
            assert(dataStore is ChatsRemoteDataStore)
        }
    }

}