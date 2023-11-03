package com.zjutjh.ijh.network.cookie

import android.util.Log
import com.zjutjh.ijh.datastore.IJhPreferenceDataSource
import com.zjutjh.ijh.datastore.model.localSession
import com.zjutjh.ijh.datastore.model.weJhSessionOrNull
import com.zjutjh.ijh.network.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.internal.http.HttpDate
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * To simplify the cookie/session management, we use a cookie jar bind to the [IJhPreferenceDataSource].
 * Although theoretically no other data sources are allowed in the network module.
 * Therefore, we strictly enforce that only [CookieJar] can access local data sources.
 */
@Singleton
class WeJhCookieJar @Inject constructor(private val local: IJhPreferenceDataSource) :
    CookieJar {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val tokenFlow: StateFlow<String?> = local.data
        .map { it.weJhSessionOrNull?.token ?: String() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        val cookie = if (url.host() == COOKIE_DOMAIN) {
            cookies.find { cookie: Cookie -> cookie.name() == COOKIE_NAME } ?: return
        } else return

        val value = cookie.value()
        val expiresAt = cookie.expiresAt()

        // Invalid Set-Cookie
        if (value.isEmpty() || expiresAt == HttpDate.MAX_DATE) return

        val localSession = localSession {
            this.token = value
            this.expirationTime = expiresAt / 1000 // Millis to Seconds
        }
        scope.launch {
            local.setSession(localSession)
            Log.i("WeJHCookieJar", "Saved cookie: $cookie")
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val token =
            tokenFlow.value
                ?: throw IOException("Uninitialized token, maybe still is loading.")
        if (token.isEmpty()) {
            return emptyList()
        }
        return listOf(
            Cookie.Builder()
                .domain(COOKIE_DOMAIN)
                .name(COOKIE_NAME)
                .value(token)
                .build()
        )
    }

    companion object {
        const val COOKIE_NAME = "wejh-session"
        const val COOKIE_DOMAIN = BuildConfig.WE_JH_COOKIE_DOMAIN
    }
}