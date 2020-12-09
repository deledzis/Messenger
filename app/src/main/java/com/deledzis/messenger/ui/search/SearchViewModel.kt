package com.deledzis.messenger.ui.search

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.util.debounce
import com.deledzis.messenger.util.onTextChangedDebounced
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class SearchViewModel(private val chatId: Int) : BaseViewModel() {
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
                        val response = repository.search(chatId, it)
                        if (response == null) {
                            messages.postValue(emptyList())
                        } else {
                            messages.postValue(response.messages)
                        }
                        stopLoading()
                    }
                }
        }
    }
}