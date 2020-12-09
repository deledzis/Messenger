package com.deledzis.messenger.ui.addchat

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.util.debounce
import com.deledzis.messenger.util.onTextChangedDebounced
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class AddChatViewModel : BaseViewModel() {
    override val repository: AddChatRepository = AddChatRepository(App.injector.api())

    val searchText = MutableLiveData<String>()
    val users = MutableLiveData<List<User>>()
    val addedChat = MutableLiveData<ChatReduced>()

    fun init(editText: EditText) {
        scope.launch(Dispatchers.Main) {
            editText.onTextChangedDebounced()
                .debounce(500)
                .consumeEach {
                    searchText.postValue(it)
                    if (it.isBlank()) {
                        users.postValue(null)
                    } else {
                        startLoading()
                        val response = repository.getAvailableUsers(it)
                        if (response == null) {
                            users.postValue(emptyList())
                        } else {
                            users.postValue(response.users)
                        }
                        stopLoading()
                    }
                }
        }
    }

    fun addChat(user: User) {
        scope.launch {
            startLoading()
            val response = repository.addChat(
                userId = App.injector.userData().authorizedUser?.id ?: 0,
                interlocutorId = user.id
            )
            addedChat.postValue(response)
            stopLoading()
        }
    }
}