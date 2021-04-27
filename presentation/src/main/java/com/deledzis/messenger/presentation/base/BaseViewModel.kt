package com.deledzis.messenger.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.common.usecase.ResponseErrorException
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.infrastructure.util.SingleEventLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import timber.log.Timber
import java.io.Serializable
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope, Serializable {

    private val job = Job()
    protected abstract val receiveChannel: ReceiveChannel<Response<Entity, Error>>

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingError: MutableLiveData<Boolean> = MutableLiveData(false)
    val connectionError = SingleEventLiveData<Boolean>()
    val authError = SingleEventLiveData<Boolean>()

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
        stopLoading()
        Timber.e("Handle Failure: $error")
        error.asNetworkError?.let {
            GlobalScope.launch {
                connectionError.postValue(true)
            }
        }
        error.exception?.asHttpError?.let {
            if (it.isAuthError) {
                GlobalScope.launch {
                    authError.postValue(true)
                }
            }
        }
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


    override fun onCleared() {
        Timber.d("onCleared: $this")
        receiveChannel.cancel()
        coroutineContext.cancel()
        stopLoading()
        super.onCleared()
    }

    companion object {
        val Error?.asNetworkError: Error.NetworkError?
            get() = if (this is Error.NetworkError) this else null

        val Exception?.asHttpError: ResponseErrorException?
            get() = if (this is ResponseErrorException) this else null

        val ResponseErrorException.isGeneralError: Boolean
            get() = this.errorCode == 400
        val ResponseErrorException.isMissingLoginError: Boolean
            get() = this.errorCode == 401
        val ResponseErrorException.isMissingPasswordError: Boolean
            get() = this.errorCode == 402
        val ResponseErrorException.isWrongCredentialsError: Boolean
            get() = this.errorCode == 403 || this.errorCode == 404
        val ResponseErrorException.isUserAlreadyExistsError: Boolean
            get() = this.errorCode == 405
        val ResponseErrorException.isAuthError: Boolean
            get() = this.errorCode == 406
        val ResponseErrorException.isInterlocutorNotFoundError: Boolean
            get() = this.errorCode == 407
        val ResponseErrorException.isChatNotFoundError: Boolean
            get() = this.errorCode == 408
        val ResponseErrorException.isMissingUserError: Boolean
            get() = this.errorCode == 409
        val ResponseErrorException.isMissingInterlocutorError: Boolean
            get() = this.errorCode == 410
        val ResponseErrorException.isMissingChatError: Boolean
            get() = this.errorCode == 411
        val ResponseErrorException.isSendMessageError: Boolean
            get() = this.errorCode == 412
        val ResponseErrorException.isMissingCurrentPasswordError: Boolean
            get() = this.errorCode == 413
        val ResponseErrorException.isWrongPasswordError: Boolean
            get() = this.errorCode == 414
        val ResponseErrorException.isUpdateUserError: Boolean
            get() = this.errorCode == 415
        val ResponseErrorException.isDialogAlreadyCreatedError: Boolean
            get() = this.errorCode == 416
        val ResponseErrorException.isMissingMessageTypeError: Boolean
            get() = this.errorCode == 417
        val ResponseErrorException.isMissingMessageContentError: Boolean
            get() = this.errorCode == 418
        val ResponseErrorException.isDeleteUserError: Boolean
            get() = this.errorCode == 419
        val ResponseErrorException.isDeleteChatError: Boolean
            get() = this.errorCode == 420
        val ResponseErrorException.isMissingMessageIdError: Boolean
            get() = this.errorCode == 421
        val ResponseErrorException.isDeleteMessageError: Boolean
            get() = this.errorCode == 422

        val ResponseErrorException.isLoginError: Boolean
            get() = this.isMissingLoginError || this.isMissingPasswordError || this.isWrongCredentialsError
    }
}