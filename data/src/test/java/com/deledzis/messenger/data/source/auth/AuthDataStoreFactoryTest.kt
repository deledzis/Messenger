package com.deledzis.messenger.data.source.auth

import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.Test

internal class AuthDataStoreFactoryTest {

    private val remoteDataStore = mockk<AuthRemoteDataStore>()
    private val factory by lazy { AuthDataStoreFactory(remoteDataStore) }

    @Test
    fun retrieveDataStore() {
        val dataStore = factory.retrieveDataStore()
        Assert.assertTrue(dataStore is AuthRemoteDataStore)
    }

}