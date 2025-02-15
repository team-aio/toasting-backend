package io.toasting.global.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class Jwt(
    @Value("\${spring.jwt.secret}") private val secret: String,
) {
    private val log = KotlinLogging.logger {}

    fun createAccessToken(
        username: String,
        role: String,
        expiredMs: Long,
    ): String =
        JWT
            .create()
            .withClaim("memberId", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + expiredMs))
            .withIssuedAt(Date(System.currentTimeMillis()))
            .sign(Algorithm.HMAC256(secret))

    fun role(accessToken: String): String? =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .getClaim("role")
                .asString()
        }.onFailure {
            log.warn { "Token verification failed: ${it.message}" }
        }.getOrNull()

    fun memberId(accessToken: String): String? =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .getClaim("memberId")
                .asString()
        }.onFailure {
            log.warn { "Token verification failed: ${it.message}" }
        }.getOrNull()

    fun isExpired(accessToken: String): Boolean =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .expiresAt
                .before(Date(System.currentTimeMillis()))
        }.getOrDefault(false)
}
