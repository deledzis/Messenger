package com.deledzis.messenger.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope, Serializable {

    @Inject
    lateinit var analytics: FirebaseAnalytics

    private val job = Job()
    protected abstract val receiveChannel: ReceiveChannel<Response<Entity, Error>>

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingError: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData<String>()

    abstract suspend fun resolve(value: Response<Entity, Error>)

    init {
        processStream()
    }

    private fun processStream() {
        launch {
            receiveChannel.consumeEach {
                resolve(it)
            }
        }
    }

    protected open fun startLoading() {
        loading.postValue(true)
        loadingError.postValue(false)
    }

    protected open fun stopLoading(error: Boolean = false) {
        loading.postValue(false)
        loadingError.postValue(error)
    }

    protected open fun handleFailure(error: Error) {
        Timber.e("Handle Failure: ${error.exception}")
        this.error.postValue(error.exception?.message)
        logException(exception = error.exception)
    }

    protected open fun handleState(state: Response.State) {
        when (state) {
            is Response.State.Loading -> startLoading()
            is Response.State.Loaded -> stopLoading()
        }
    }

    protected fun logException(exception: Exception?) {
        Timber.e("Error $exception")
    }

    protected fun logEvent(name: String, params: Array<Pair<String, String>>) {
        Timber.i("[Analytics::$name] $params")
        analytics.logEvent(name) {
            params.forEach {
                param(it.first, it.second)
            }
        }
    }

    override fun onCleared() {
        Timber.d("onCleared: $this")
        receiveChannel.cancel()
        coroutineContext.cancel()
        stopLoading()
        super.onCleared()
    }
}