package com.zjutjh.ijh.util

/**
 * Kotlin-std Result may have some unexpected issues while using coroutine
 * Using self-implemented [Result] instead.
 */
sealed interface Result<out T> {
    class Success<T>(val value: T) : Result<T>
    class Failure(val error: Throwable) : Result<Nothing>
}

inline fun <R, T> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(this.value))
        is Result.Failure -> Result.Failure(this.error)
    }
}
