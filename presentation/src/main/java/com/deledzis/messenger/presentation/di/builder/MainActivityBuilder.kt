package com.deledzis.messenger.presentation.di.builder

import com.deledzis.messenger.common.di.scopes.ActivityScope
import com.deledzis.messenger.presentation.di.module.NavHostModule
import com.deledzis.messenger.presentation.screens.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            NavHostModule::class,
            DialogFragmentBindingModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity

}