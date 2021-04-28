package com.deledzis.messenger.data.source.auth

import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class AuthDataStoreFactoryTest {

    private val factory by lazy { AuthDataStoreFactory(mockk()) }

    @Test
    fun retrieveDataStore() {
        assertDoesNotThrow {
            val dataStore = factory.retrieveDataStore()
            assert(dataStore is AuthRemoteDataStore)
        }
    }

}