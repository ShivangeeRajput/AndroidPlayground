package com.example.androidplayground.core

import kotlinx.coroutines.CancellationException

/** Like runCatching, but never swallows coroutine cancellation. */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
