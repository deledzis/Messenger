package com.deledzis.messenger.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {
    protected val parentJob = Job()
    protected val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    protected val scope = CoroutineScope(coroutineContext)

    protected abstract val repository: BaseRepository

    val loading = MutableLiveData<Boolean>()
    val loadingError = MutableLiveData<Boolean>()

    protected open fun startLoading() {
        loading.postValue(true)
        loadingError.postValue(false)
    }

    protected open fun stopLoading(error: Boolean = false) {
        loading.postValue(false)
        loadingError.postValue(error)
    }

    protected open fun cancelAllRequests() {
        stopLoading()
        coroutineContext.cancel()
    }

    override fun onCleared() {
        cancelAllRequests()
        super.onCleared()
    }
}