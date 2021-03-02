package com.deledzis.messenger.old.util.extensions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun MutableLiveData<Int>.inc() {
    this.value = (this.value ?: 0) + 1
}