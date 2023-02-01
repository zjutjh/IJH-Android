package com.zjutjh.ijh.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

sealed interface LoadResult<out T> {
    object Loading : LoadResult<Nothing>
    class Ready<T>(val data: T) : LoadResult<T>

    fun <T> isEqual(v: LoadResult<T>): Boolean =
        if (this is Ready && v is Ready) {
            this.data == v.data
        } else this is Loading && v is Loading

    fun <R> isEqual(v: LoadResult<R>, areEquivalent: (left: T, right: R) -> Boolean): Boolean =
        if (this is Ready && v is Ready) {
            areEquivalent(this.data, v.data)
        } else this is Loading && v is Loading
}

fun <T> LoadResult<T>.isLoading(): Boolean = this is LoadResult.Loading

fun <T> LoadResult<T>.isReady(): Boolean = this is LoadResult.Ready

fun <T> Flow<T>.asLoadResultFlow(): Flow<LoadResult<T>> = this
    .map<T, LoadResult<T>> {
        LoadResult.Ready(it)
    }
    .onStart { emit(LoadResult.Loading) }

fun <T> Flow<T>.asLoadResultSharedFlow(
    scope: CoroutineScope,
    started: SharingStarted
): SharedFlow<LoadResult<T>> = this
    .map<T, LoadResult<T>> {
        LoadResult.Ready(it)
    }
    .onStart { emit(LoadResult.Loading) }
    .shareIn(
        scope = scope,
        started = started,
        replay = 1
    )


fun <T> Flow<T>.asLoadResultStateFlow(
    scope: CoroutineScope,
    started: SharingStarted
): StateFlow<LoadResult<T>> = this
    .map<T, LoadResult<T>> {
        LoadResult.Ready(it)
    }
    .stateIn(
        scope = scope,
        started = started,
        initialValue = LoadResult.Loading
    )

