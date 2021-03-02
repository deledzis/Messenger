package com.deledzis.messenger.presentation.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.deledzis.messenger.presentation.base.InjectingFragmentFactory
import com.deledzis.messenger.presentation.di.key.FragmentKey
import com.deledzis.messenger.presentation.features.addchat.AddChatFragment
import com.deledzis.messenger.presentation.features.chat.ChatFragment
import com.deledzis.messenger.presentation.features.chats.ChatsFragment
import com.deledzis.messenger.presentation.features.login.LoginFragment
import com.deledzis.messenger.presentation.features.register.RegisterFragment
import com.deledzis.messenger.presentation.features.search.SearchFragment
import com.deledzis.messenger.presentation.features.settings.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentBindingModule {

    @Binds
    abstract fun bindFragmentFactory(factory: InjectingFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(AddChatFragment::class)
    abstract fun bindAddChatFragment(fragment: AddChatFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ChatFragment::class)
    abstract fun bindChatFragment(fragment: ChatFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ChatsFragment::class)
    abstract fun bindChatsFragment(fragment: ChatsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    abstract fun bindLoginFragment(fragment: LoginFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RegisterFragment::class)
    abstract fun bindRegisterFragment(fragment: RegisterFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SearchFragment::class)
    abstract fun bindSearchFragment(fragment: SearchFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(fragment: SettingsFragment): Fragment

}