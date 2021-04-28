package com.deledzis.messenger.di.module

import com.deledzis.messenger.data.mapper.ServerMessageResponseMapper
import com.deledzis.messenger.data.mapper.auth.AuthMapper
import com.deledzis.messenger.data.mapper.chats.ChatMapper
import com.deledzis.messenger.data.mapper.chats.ChatsMapper
import com.deledzis.messenger.data.mapper.messages.MessagesMapper
import com.deledzis.messenger.data.mapper.users.UserMapper
import com.deledzis.messenger.data.mapper.users.UsersMapper
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.chats.ChatsRepositoryImpl
import com.deledzis.messenger.data.repository.messages.MessagesRepositoryImpl
import com.deledzis.messenger.data.repository.users.UsersRepositoryImpl
import com.deledzis.messenger.data.source.auth.AuthDataStoreFactory
import com.deledzis.messenger.data.source.chats.ChatsDataStoreFactory
import com.deledzis.messenger.data.source.messages.MessagesDataStoreFactory
import com.deledzis.messenger.data.source.users.UsersDataStoreFactory
import com.deledzis.messenger.domain.model.BaseNetworkManager
import dagger.Module
import dagger.Provides

@Module
internal class TestRepositoriesModule {

    @Provides
    internal fun provideAuthRepositoryImpl(
        factory: AuthDataStoreFactory,
        authMapper: AuthMapper,
        serverMessageResponseMapper: ServerMessageResponseMapper,
        networkManager: BaseNetworkManager
    ): AuthRepositoryImpl {
        return AuthRepositoryImpl(
            factory = factory,
            authMapper = authMapper,
            serverMessageResponseMapper = serverMessageResponseMapper,
            networkManager = networkManager
        )
    }

    @Provides
    internal fun provideMessagesRepositoryImpl(
        factory: MessagesDataStoreFactory,
        itemsMapper: MessagesMapper,
        serverMessageResponseMapper: ServerMessageResponseMapper,
        networkManager: BaseNetworkManager
    ): MessagesRepositoryImpl {
        return MessagesRepositoryImpl(
            factory = factory,
            itemsMapper = itemsMapper,
            serverMessageResponseMapper = serverMessageResponseMapper,
            networkManager = networkManager
        )
    }

    @Provides
    internal fun provideChatsRepositoryImpl(
        factory: ChatsDataStoreFactory,
        itemMapper: ChatMapper,
        itemsMapper: ChatsMapper,
        serverMessageResponseMapper: ServerMessageResponseMapper,
        networkManager: BaseNetworkManager
    ): ChatsRepositoryImpl {
        return ChatsRepositoryImpl(
            factory,
            itemMapper,
            itemsMapper,
            serverMessageResponseMapper,
            networkManager
        )
    }

    @Provides
    internal fun provideUsersRepositoryImpl(
        factory: UsersDataStoreFactory,
        itemMapper: UserMapper,
        itemsMapper: UsersMapper,
        networkManager: BaseNetworkManager
    ): UsersRepositoryImpl {
        return UsersRepositoryImpl(
            factory = factory,
            itemMapper = itemMapper,
            itemsMapper = itemsMapper,
            networkManager = networkManager
        )
    }

}