package io.toasting.domain.member.application

import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.output.ReIssueOutput
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.security.jwt.JwtFactory
import org.springframework.stereotype.Service

@Service
class ReIssueService(
    private val jwtFactory: JwtFactory,
) {
    private val log = KotlinLogging.logger {}

    fun reIssueAccessToken(refreshToken: String): ReIssueOutput {
        var output: ReIssueOutput? = null

        jwtFactory
            .validateRefreshToken(refreshToken)
            .onSuccess {
                val (newAccessToken, newRefreshToken) = createNewAccessAndRefreshToken(refreshToken)
                output = ReIssueOutput(newAccessToken, newRefreshToken)
            }.onFailure { exception -> findException(exception) }

        return output ?: throw IllegalStateException("Unexpected error occurred")
    }

    private fun findException(exception: Throwable): Nothing {
        when (exception) {
            is AuthExceptionHandler.TokenNotFoundException -> {
                throw AuthExceptionHandler.TokenNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND)
            }

            is AuthExceptionHandler.TokenExpiredException -> {
                throw AuthExceptionHandler.TokenExpiredException(ErrorStatus.REFRESH_TOKEN_EXPIRED)
            }

            else -> {
                log.error { "Unknown exception: ${exception.message}" }
                return throw exception
            }
        }
    }

    private fun createNewAccessAndRefreshToken(refreshToken: String): Pair<String, String> {
        val memberId =
            jwtFactory.memberId(refreshToken) ?: run {
                log.error { "memberId is null" }
                throw IllegalArgumentException("memberId is null")
            }
        val role =
            jwtFactory.role(refreshToken) ?: run {
                log.error { "role is null" }
                throw IllegalArgumentException("role is null")
            }

        val newAccessToken = jwtFactory.createAccessToken(memberId, role)
        val newRefreshToken = jwtFactory.createRefreshToken(memberId, role)

        return newAccessToken to newRefreshToken
    }
}
