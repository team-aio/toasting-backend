package io.toasting.global.extension

import io.toasting.global.constants.Auth
import jakarta.servlet.http.Cookie

class CookieExtension {
    companion object
}

fun CookieExtension.Companion.createCookie(
    key: String,
    value: String,
    cookieExpiredSeconds: Int,
): Cookie =
    Cookie(key, value).apply {
        isHttpOnly = true
        maxAge = cookieExpiredSeconds
        path = "/"
    }

fun Array<Cookie>.findRefreshTokenOrNull(): String? = this.find { it.name == Auth.REFRESH_TOKEN }?.value
