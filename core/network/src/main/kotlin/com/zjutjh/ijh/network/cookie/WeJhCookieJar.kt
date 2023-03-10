package com.zjutjh.ijh.network.cookie

import android.util.Log
import com.zjutjh.ijh.datastore.WeJhPreferenceDataSource
import com.zjutjh.ijh.datastore.model.WeJhPreferenceKt
import com.zjutjh.ijh.datastore.model.sessionOrNull
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
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeJhCookieJar @Inject constructor(private val local: WeJhPreferenceDataSource) :
    CookieJar {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val tokenFlow: StateFlow<String?> = local.data
        .map { it.sessionOrNull?.token ?: String() }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        val cookie = if (url.host() == COOKIE_DOMAIN) {
            cookies.find { cookie: Cookie -> cookie.name() == COOKIE_NAME } ?: return
        } else return
        val localSession = WeJhPreferenceKt.session {
            this.token = cookie.value()
            this.expirationTime = cookie.expiresAt()
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
        const val COOKIE_DOMAIN = "wejh.zjutjh.com"
    }
}