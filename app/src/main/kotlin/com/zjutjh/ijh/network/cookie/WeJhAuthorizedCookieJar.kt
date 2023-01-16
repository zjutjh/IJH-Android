package com.zjutjh.ijh.network.cookie

import com.zjutjh.ijh.datastore.WeJhUserLocalDataSource
import com.zjutjh.ijh.network.exception.UnauthorizedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeJhAuthorizedCookieJar @Inject constructor(local: WeJhUserLocalDataSource) : CookieJar {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val tokenFlow: Flow<String> = local.user.map { it.sessionToken }

    init {
        scope.launch {
            tokenFlow.collect {
                currentToken = it
            }
        }
    }

    @Volatile
    private var currentToken: String? = null

    /**
     * For requests only
     */
    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) = Unit

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val token =
            currentToken
                ?: throw UnauthorizedException("Uninitialized token, maybe still in loading.")
        if (token.isEmpty()) {
            throw UnauthorizedException("Empty session token")
        }
        return mutableListOf(
            Cookie.Builder()
                .domain(COOKIE_DOMAIN)
                .name(COOKIE_NAME)
                .value(token)
                .build()
        )
    }

    companion object {
        const val COOKIE_NAME = "wejh-session"
        const val COOKIE_DOMAIN = "jh.zjut.edu.cn"
    }
}