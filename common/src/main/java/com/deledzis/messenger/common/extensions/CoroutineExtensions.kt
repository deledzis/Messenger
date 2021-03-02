package com.deledzis.messenger.common.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

fun <T> CoroutineScope.mergeChannels(vararg channels: ReceiveChannel<T>): ReceiveChannel<T> {
    return produce {
        channels.forEach {
            launch { it.consumeEach { send(it) } }
        }
    }
}

fun <T> mergeFlows(vararg flows: Flow<T>): Flow<T> = flowOf(*flows).flattenMerge()