package com.zjutjh.ijh.network.cookie

import android.util.Log
import com.zjutjh.ijh.datastore.WeJhUserLocalDataSource
import com.zjutjh.ijh.network.exception.UnauthorizedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject

class WeJhAuthorizationCookieJar @Inject constructor(private val local: WeJhUserLocalDataSource) :
    CookieJar {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            subscribe()
        }
    }

    private suspend fun subscribe() =
        local.user
            .map {
                Cookie.Builder()
                    .name(COOKIE_NAME)
                    .value(it.sessionToken)
                    .domain(COOKIE_DOMAIN)
                    .build()
            }
            .collectLatest {
                Log.d("WeJhAuthCookieJar", "Current token: $it")
                cookie = it
            }

    /**
     * Null stand for Loading
     */
    private var cookie: Cookie = Cookie.Builder()
        .name(COOKIE_NAME)
        .value(String())
        .domain(COOKIE_DOMAIN)
        .build()

    /**
     * For requests only
     */
    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) = Unit

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        if (cookie.value().isEmpty()) {
            throw UnauthorizedException("Empty session token")
        }
        return mutableListOf(
            cookie
        )
    }

    companion object {
        const val COOKIE_NAME = "wejh-session"
        const val COOKIE_DOMAIN = "jh.zjut.edu.cn"
    }
}