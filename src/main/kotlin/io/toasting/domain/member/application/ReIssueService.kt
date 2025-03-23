package io.toasting.domain.member.application

import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.output.ReIssueOutput
import io.toasting.domain.member.entity.RefreshToken
import io.toasting.domain.member.repository.RefreshTokenRepository
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.security.jwt.JwtFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Service
class ReIssueService(
    private val jwtFactory: JwtFactory,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    private val log = KotlinLogging.logger {}

    fun reIssueAccessToken(refreshToken: String): ReIssueOutput {
        var output: ReIssueOutput? = null
        // TODO : 밑에 validateRefreshToken 역할이 있는데, 여기서 따로 validate하는게 맞는건지? 추후 리팩토링 해야함
        if (!refreshTokenRepository.existsByToken(refreshToken)) {
            throw AuthExceptionHandler.TokenNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND)
        }

        jwtFactory
            .validateRefreshToken(refreshToken)
            .onSuccess {
                val (newAccessToken, newRefreshToken) = createNewAccessAndRefreshToken(refreshToken)
                replaceRefreshToken(refreshToken, newRefreshToken)
                output = ReIssueOutput(newAccessToken, newRefreshToken)
            }.onFailure { exception -> findException(exception) }

        return output ?: throw IllegalStateException("Unexpected error occurred")
    }

    private fun replaceRefreshToken(
        oldRefreshToken: String,
        newRefreshToken: String,
    ) {
        refreshTokenRepository.deleteByToken(oldRefreshToken)
        val memberId = jwtFactory.memberId(newRefreshToken) ?: throw IllegalArgumentException("memberId is null")
        val date = Date(System.currentTimeMillis() + jwtFactory.refreshExpiredMs())

        val refreshToken =
            RefreshToken(
                memberId = memberId.toLong(),
                token = newRefreshToken,
                expiredAt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()),
            )

        refreshTokenRepository.save(refreshToken)
    }

    private fun findException(exception: Throwable) {
        when (exception) {
            is AuthExceptionHandler.TokenNotFoundException -> {
                throw AuthExceptionHandler.TokenNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND)
            }

            is AuthExceptionHandler.TokenExpiredException -> {
                throw AuthExceptionHandler.TokenExpiredException(ErrorStatus.REFRESH_TOKEN_EXPIRED)
            }

            else -> {
                log.error { "Unknown exception: ${exception.message}" }
                throw exception
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
