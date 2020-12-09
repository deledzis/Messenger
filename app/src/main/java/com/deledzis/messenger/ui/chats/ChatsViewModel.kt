package com.deledzis.messenger.ui.chats

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.util.fromJson
import kotlinx.coroutines.launch

class ChatsViewModel : BaseViewModel() {
    override val repository: ChatsRepository = ChatsRepository(App.injector.api())

    val error = MutableLiveData<Boolean>()
    val chats = MutableLiveData<List<ChatReduced>>()

    fun getChats(refresh: Boolean = true) {
        error.postValue(null)
        if (refresh || chats.value.isNullOrEmpty()) startLoading()
        scope.launch {
            val response = repository.getChats()
            if (response == null) {
                error.postValue(true)
            } else {
                chats.postValue(response.chats)
            }
            stopLoading()
        }
    }

    fun handleBackgroundChatsResult(chatsJson: String?) {
        chatsJson ?: return
        val list = fromJson<List<ChatReduced>>(chatsJson)
        chats.postValue(list)
        if (list.isNotEmpty()) {
            error.postValue(null)
        }
    }
}