package com.deledzis.messenger.ui.main.viewmodel

import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.di.model.UserData
import com.deledzis.messenger.ui.main.repository.MainRepository

class MainViewModel(val userData: UserData) : BaseViewModel() {
    override val repository: MainRepository = MainRepository(App.injector.api())
}
