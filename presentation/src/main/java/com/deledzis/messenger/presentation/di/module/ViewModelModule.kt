package com.deledzis.messenger.presentation.di.module

import androidx.lifecycle.ViewModel
import com.deledzis.messenger.presentation.di.key.ViewModelKey
import com.deledzis.messenger.presentation.screens.addchat.AddChatViewModel
import com.deledzis.messenger.presentation.screens.chat.ChatViewModel
import com.deledzis.messenger.presentation.screens.chats.ChatsViewModel
import com.deledzis.messenger.presentation.screens.login.LoginViewModel
import com.deledzis.messenger.presentation.screens.main.MainActivityViewModel
import com.deledzis.messenger.presentation.screens.main.UserViewModel
import com.deledzis.messenger.presentation.screens.register.RegisterViewModel
import com.deledzis.messenger.presentation.screens.search.SearchViewModel
import com.deledzis.messenger.presentation.screens.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    internal abstract fun bindUserViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddChatViewModel::class)
    internal abstract fun bindAddChatViewModel(viewModel: AddChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindChatViewModel(viewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatsViewModel::class)
    internal abstract fun bindChatsViewModel(viewModel: ChatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}