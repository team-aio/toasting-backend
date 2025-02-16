package io.toasting.global.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtFactory(
    @Value("\${spring.jwt.secret}") private val secret: String,
    @Value("\${spring.jwt.access-token-expired-ms}") private val accessExpiredMs: Long,
    @Value("\${spring.jwt.refresh-token-expired-ms}") private val refreshExpiredMs: Long,
) {
    private val log = KotlinLogging.logger {}

    fun createAccessToken(
        username: String,
        role: String,
        accessExpiredMs: Long = this.accessExpiredMs,
    ): String =
        JWT
            .create()
            .withClaim("memberId", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + accessExpiredMs))
            .withIssuedAt(Date(System.currentTimeMillis()))
            .sign(Algorithm.HMAC256(secret))

    fun createRefreshToken(
        username: String,
        role: String,
        refreshExpiredMs: Long = this.refreshExpiredMs,
    ): String =
        JWT
            .create()
            .withClaim("memberId", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + refreshExpiredMs))
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
