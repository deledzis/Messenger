package com.deledzis.messenger.di.module

import android.app.Application
import com.deledzis.messenger.App

class TestApplicationModule : ApplicationModule() {

    override fun bindApplication(app: App): Application = App()

}