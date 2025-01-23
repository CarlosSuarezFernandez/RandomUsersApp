package com.carlosdev.randomusersapp.utils

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FlowTest<T>(channel: Channel<T>) {

    private val iterator = channel.iterator()

    suspend fun next(): T = if (iterator.hasNext()) {
        iterator.next()
    } else illegalState()
}

suspend fun <T> Flow<T>.test(body: suspend FlowTest<T>.() -> Unit) {
    val flow: Flow<T> = this@test
    val channel = Channel<T>(Channel.UNLIMITED, onBufferOverflow = BufferOverflow.SUSPEND)
    coroutineScope {
        val job = launch(start = CoroutineStart.UNDISPATCHED, context = Unconfined) {
            flow.collect { item: T ->
                channel.send(item)
            }
            channel.close()
        }

        FlowTest(channel).run {
            body()
        }

        job.cancel()
    }
}

private const val FLOW_TEST_TIMEOUT = 100L

fun illegalState(message: String? = null): Nothing = throw IllegalStateException(message)
