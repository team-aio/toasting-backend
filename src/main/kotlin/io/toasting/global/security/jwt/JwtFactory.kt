package io.toasting.global.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.api.code.status.ErrorStatus
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.constants.Auth
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
            .withClaim(Auth.CATEGORY, Auth.ACCESS_TOKEN)
            .withClaim(Auth.MEMBER_ID, username)
            .withClaim(Auth.ROLE, role)
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
            .withClaim(Auth.CATEGORY, Auth.REFRESH_TOKEN)
            .withClaim(Auth.MEMBER_ID, username)
            .withClaim(Auth.ROLE, role)
            .withExpiresAt(Date(System.currentTimeMillis() + refreshExpiredMs))
            .withIssuedAt(Date(System.currentTimeMillis()))
            .sign(Algorithm.HMAC256(secret))

    fun role(accessToken: String): String? =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .getClaim(Auth.ROLE)
                .asString()
        }.onFailure {
            log.error { "Token verification failed: ${it.message}" }
        }.getOrNull()

    fun memberId(accessToken: String): String? =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .getClaim(Auth.MEMBER_ID)
                .asString()
        }.onFailure {
            log.error { "Token verification failed: ${it.message}" }
        }.getOrNull()

    fun validateAccessToken(token: String): Result<Unit> {
        if (isExpired(token)) {
            log.info { "token is Expired, token = $token" }
            return Result.failure(AuthExceptionHandler.TokenExpiredException(ErrorStatus.ACCESS_TOKEN_EXPIRED))
        }
        if (category(token) != Auth.ACCESS_TOKEN) {
            log.error { "token is not accessToken, accessToken = $token" }
            return Result.failure(AuthExceptionHandler.TokenNotFoundException(ErrorStatus.ACCESS_TOKEN_NOT_FOUND))
        }
        return Result.success(Unit)
    }

    private fun category(token: String): String? =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim(Auth.CATEGORY)
                .asString()
        }.onFailure {
            log.error { "Token verification failed: ${it.message}" }
        }.getOrNull()

    private fun isExpired(accessToken: String): Boolean =
        runCatching {
            JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .expiresAt
                .before(Date(System.currentTimeMillis()))
        }.getOrDefault(false)
}
