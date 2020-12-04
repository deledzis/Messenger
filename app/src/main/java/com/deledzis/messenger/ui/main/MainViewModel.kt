package com.deledzis.messenger.ui.main

import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.di.model.UserData

class MainViewModel(val userData: UserData) : BaseViewModel() {
    override val repository: MainRepository = MainRepository()
}
