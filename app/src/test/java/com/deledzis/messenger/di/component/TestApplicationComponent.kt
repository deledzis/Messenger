package com.deledzis.messenger.di.component

import com.deledzis.messenger.AppInjectTest
import com.deledzis.messenger.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class
    ]
)
interface TestApplicationComponent {

    fun into(test: AppInjectTest)

}