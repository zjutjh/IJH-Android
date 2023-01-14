package com.zjutjh.ijh.network

import com.zjutjh.ijh.network.exception.UnauthorizedException
import com.zjutjh.ijh.network.model.NetworkWeJhUser
import com.zjutjh.ijh.network.service.WeJhAuthorizedService
import com.zjutjh.ijh.network.service.WeJhService
import com.zjutjh.ijh.network.service.request.LoginBody
import okhttp3.Cookie
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

class WeJhUserNetworkDataSource @Inject constructor(
    private val weJhService: WeJhService,
    private val weJhAuthorizedService: WeJhAuthorizedService
) {

    suspend fun login(username: String, password: String): NetworkWeJhUser {
        val response = weJhService.login(LoginBody(username, password))
        val result = response.body()!!

        // Fetch cookie settings
        val cookies: List<Cookie> =
            Cookie.parseAll(response.raw().request().url(), response.headers())
        val sessionCookie =
            cookies.find { cookie -> cookie.name().equals("wejh-session") }
                ?: throw UnauthorizedException("No session token obtained.")

        val sessionToken = sessionCookie.value()
        val sessionExpiresAt =
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(sessionCookie.expiresAt()),
                ZoneOffset.UTC
            )

        return result.user.apply {
            this.sessionToken = sessionToken
            this.sessionExpiresAt = sessionExpiresAt
        }
    }


    suspend fun getUserInfo(): NetworkWeJhUser =
        weJhAuthorizedService.getUserInfo().user

}