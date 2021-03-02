package com.deledzis.messenger.infrastructure.di

import android.content.Context
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.infrastructure.services.NetworkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {

    @Singleton
    @Provides
    fun provideNetworkManager(context: Context): BaseNetworkManager {
        return NetworkManager(context = context)
    }
}