package com.deledzis.messenger.data.di

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
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.repository.ChatsRepository
import com.deledzis.messenger.domain.repository.MessagesRepository
import com.deledzis.messenger.domain.repository.UsersRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesTestModule {
    @Provides
    internal fun provideAuthRepository(
        factory: AuthDataStoreFactory,
        authMapper: AuthMapper,
        networkManager: BaseNetworkManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            factory = factory,
            authMapper = authMapper,
            networkManager = networkManager
        )
    }

    @Provides
    internal fun provideMessagesRepository(
        factory: MessagesDataStoreFactory,
        itemsMapper: MessagesMapper,
        serverMessageResponseMapper: ServerMessageResponseMapper,
        networkManager: BaseNetworkManager
    ): MessagesRepository {
        return MessagesRepositoryImpl(
            factory = factory,
            itemsMapper = itemsMapper,
            serverMessageResponseMapper = serverMessageResponseMapper,
            networkManager = networkManager
        )
    }

    @Provides
    internal fun provideChatsRepository(
        factory: ChatsDataStoreFactory,
        itemMapper: ChatMapper,
        itemsMapper: ChatsMapper,
        networkManager: BaseNetworkManager
    ): ChatsRepository {
        return ChatsRepositoryImpl(
            factory = factory,
            itemMapper = itemMapper,
            itemsMapper = itemsMapper,
            networkManager = networkManager
        )
    }

    @Provides
    internal fun provideUsersRepository(
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

@Singleton
@Component(
    modules = [
//        ApplicationModule::class,
//        NetworkModule::class,
//        UtilsModule::class,
        RepositoriesModule::class
//        CacheModule::class,
//        MainActivityBuilder::class,
//        ViewModelModule::class,
//        AndroidSupportInjectionModule::class,
//        AndroidInjectionModule::class
    ]
)
interface RepositoriesTestComponent {

    val authRepositoryImpl: AuthRepositoryImpl
}