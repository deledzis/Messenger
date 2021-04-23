package com.deledzis.messenger.data.source.users

import com.deledzis.messenger.domain.model.BaseNetworkManager
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class UsersDataStoreFactoryTest {

    private var isConnectedToInternet: Boolean = true
    private val factory by lazy {
        UsersDataStoreFactory(
            cacheDataStore = mockk(),
            remoteDataStore = mockk(),
            networkManager = object : BaseNetworkManager {
                override fun isConnectedToInternet(): Boolean = isConnectedToInternet
            }
        )
    }

    @Test
    fun retrieveDataStore() {
        assertDoesNotThrow {
            var dataStore = factory.retrieveDataStore()
            assert(dataStore is UsersRemoteDataStore)

            isConnectedToInternet = false
            dataStore = factory.retrieveDataStore()
            assert(dataStore is UsersCacheDataStore)
        }
    }

}