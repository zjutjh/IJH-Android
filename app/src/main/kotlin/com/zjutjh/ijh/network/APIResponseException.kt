package com.zjutjh.ijh.network

import java.io.IOException

/**
 * API call exception response
 */
class APIResponseException(val code: Int, val msg: String) : IOException()