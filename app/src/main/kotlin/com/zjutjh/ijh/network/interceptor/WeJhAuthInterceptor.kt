package com.zjutjh.ijh.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor for adding WeJH session cookie
 */
class WeJhAuthInterceptor @Inject constructor() :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // TODO
        val request = originalRequest.newBuilder()
            .addHeader("Cookie", "wejh-session=")
            .build()
        return chain.proceed(request)
    }
}