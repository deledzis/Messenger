package com.deledzis.messenger.data.di

import com.deledzis.messenger.data.mapper.ServerMessageResponseMapper
import com.deledzis.messenger.data.mapper.auth.AuthMapper
import com.deledzis.messenger.data.mapper.users.UserMapper
import com.deledzis.messenger.data.mapper.users.UsersMapper
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.users.UsersRepositoryImpl
import com.deledzis.messenger.data.source.auth.AuthDataStoreFactory
import com.deledzis.messenger.data.source.users.UsersDataStoreFactory
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.repository.UsersRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoriesModule {

    @Provides
    internal fun provideAuthRepository(
        factory: AuthDataStoreFactory,
        authMapper: AuthMapper,
        serverMessageResponseMapper: ServerMessageResponseMapper,
        networkManager: BaseNetworkManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            factory = factory,
            authMapper = authMapper,
            networkManager = networkManager
        )
    }

    @Provides
    internal fun provideUserRepository(
        factory: UsersDataStoreFactory,
        itemMapper: UserMapper,
        itemsMapper: UsersMapper,
        networkManager: BaseNetworkManager
    ): UsersRepository {
        return UsersRepositoryImpl(
            factory = factory,
            itemMapper = itemMapper,
            itemsMapper = itemsMapper,
            networkManager = networkManager
        )
    }
}