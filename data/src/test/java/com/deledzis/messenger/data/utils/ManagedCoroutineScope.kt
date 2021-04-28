package com.deledzis.messenger.data.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface ManagedCoroutineScope : CoroutineScope {
    fun launch(block: suspend CoroutineScope.() -> Unit): Job
}