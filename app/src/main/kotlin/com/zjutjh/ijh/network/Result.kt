package com.zjutjh.ijh.network

/**
 * Result wrapper of network response
 */
sealed class Result<out T> {
    data class Ok<out T>(val data: T) : Result<T>()
    data class Err(val code: Int, val msg: String) : Result<Nothing>()
}
