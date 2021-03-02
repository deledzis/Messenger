package com.deledzis.messenger.presentation.screens.search

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.old.App
import com.deledzis.messenger.old.base.BaseViewModel
import com.deledzis.messenger.old.util.debounce
import com.deledzis.messenger.old.util.onTextChangedDebounced
import kotlinx.coroutines.Dispatchers

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