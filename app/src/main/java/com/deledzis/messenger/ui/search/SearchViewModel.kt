package com.deledzis.messenger.ui.search

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.util.debounce
import com.deledzis.messenger.util.onTextChangedDebounced
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {
    override val repository: SearchRepository = SearchRepository(App.injector.api())

    val searchText = MutableLiveData<String>()
    val messages = MutableLiveData<List<Message>>()

    fun init(editText: EditText) {
        scope.launch(Dispatchers.Main) {
            editText.onTextChangedDebounced()
                .debounce(500)
                .consumeEach {
                    searchText.postValue(it)
                    if (it.isBlank()) {
                        messages.postValue(null)
                    } else {
                        startLoading()
                        val response = repository.search(it)
                        // artificial delay for progress while loading demonstration purposes
                        delay(1000L)
                        if (response == null) {
                            // TODO to be fixed when backend works
//                            users.postValue(emptyList())
                            messages.postValue(generateMockList())
                        } else {
                            messages.postValue(response.messages)
                        }
                        stopLoading()
                    }
                }
        }
    }

    companion object {
        fun generateMockList() = listOf(
            Message(
                id = 0,
                type = true,
                content = "",
                fileName = "some file name.docx",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 0,
                type = true,
                content = "",
                fileName = "some file name.docx",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 0,
                type = true,
                content = "",
                fileName = "some file name.docx",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 1,
                type = false,
                content = "very very very very very very very very very very very very very very very very very very very very very very very very very very long text",
                date = "2020-12-04T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 2,
                type = false,
                content = "some text ",
                date = "2020-12-03T12:25:32",
                chatId = 0,
                author = User(
                    id = 1,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 3,
                type = true,
                content = "test content",
                fileName = "very very very very very very very very long name.docx",
                date = "2020-12-02T12:25:32",
                chatId = 0,
                author = User(
                    id = 1,
                    username = "",
                    nickname = ""
                )
            ),
        )
    }
}