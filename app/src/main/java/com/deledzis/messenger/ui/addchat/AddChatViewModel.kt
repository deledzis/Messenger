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
import kotlinx.coroutines.delay
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
                        // artificial delay for progress while loading demonstration purposes
                        delay(1000L)
                        if (response == null) {
                            // TODO to be fixed when backend works
//                            users.postValue(emptyList())
                            users.postValue(generateMockList())
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
                userId = App.injector.userData().auth?.userId ?: 0,
                interlocutorId = user.id
            )
            delay(1000L)
            // TODO to be fixed when backend works
//            addedChat.postValue(response)
            addedChat.postValue(
                ChatReduced(
                    id = 5,
                    interlocutor = user,
                    lastMessage = null
                )
            )
            stopLoading()
        }
    }

    companion object {
        fun generateMockList() = listOf(
            User(
                id = 0,
                username = "user@name.com"
            ),
            User(
                id = 1,
                username = "user1@name.com"
            ),
            User(
                id = 2,
                username = "user2@name.com",
                nickname = "Denis Petrov"
            ),
            User(
                id = 3,
                username = "user3@name.com",
                nickname = "Denis Petrov"
            ),
            User(
                id = 4,
                username = "user4@name.com",
                nickname = "Denis Petrov"
            ),
            User(
                id = 5,
                username = "user5@name.com",
            ),
            User(
                id = 6,
                username = "user6@name.com",
                nickname = "Denis Petrov"
            ),
            User(
                id = 7,
                username = "user7@name.com",
                nickname = "Denis Petrov"
            ),
            User(
                id = 8,
                username = "user8@name.com",
            ),
            User(
                id = 9,
                username = "user9@name.com",
            ),
            User(
                id = 10,
                username = "user10@name.com",
                nickname = "Denis Petrov"
            ),
        )
    }
}