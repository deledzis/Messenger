package com.deledzis.messenger.ui.chats

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.Chat
import kotlinx.coroutines.launch

class ChatsViewModel : BaseViewModel() {
    override val repository: ChatsRepository = ChatsRepository(App.injector.api())

    val error = MutableLiveData<String>()

    val chats = MutableLiveData<List<Chat>>()

    fun getChats() {
        startLoading()
        scope.launch {
            val response = repository.getChats()
            if (response == null) {
                error.postValue("Не удалось обновить сообщения")
            } else {
                chats.postValue(response.chats)
            }
            stopLoading()
        }
    }
}