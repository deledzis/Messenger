package com.deledzis.messenger.old.ui.addchat

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.domain.model.entity.chats.ChatReduced
import com.deledzis.messenger.old.App
import com.deledzis.messenger.old.base.BaseViewModel
import com.deledzis.messenger.old.data.model.users.User
import com.deledzis.messenger.old.util.debounce
import com.deledzis.messenger.old.util.onTextChangedDebounced
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
                userId = App.injector.userData().authorizedUser.id ?: 0,
                interlocutorId = user.id
            )
            addedChat.postValue(response)
            stopLoading()
        }
    }
}