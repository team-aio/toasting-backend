package io.toasting.global.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object HashUtil {
    private val encoder = BCryptPasswordEncoder()

    fun encode(rawData: String): String = encoder.encode(rawData)

    fun matches(
        rawData: String,
        hashedData: String,
    ): Boolean = encoder.matches(rawData, hashedData)
}
