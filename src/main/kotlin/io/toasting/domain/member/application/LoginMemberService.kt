package io.toasting.domain.member.application

import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.input.LoginGoogleInput
import io.toasting.domain.member.application.output.LoginGoogleOutput
import io.toasting.domain.member.entity.RefreshToken
import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.exception.MemberExceptionHandler.*
import io.toasting.domain.member.repository.RefreshTokenRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import io.toasting.global.security.jwt.JwtFactory
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Service
class LoginMemberService(
    private val socialLoginRepository: SocialLoginRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtFactory: JwtFactory,
) {
    private val log = KotlinLogging.logger {}

    @Transactional
    fun loginGoogle(loginGoogleInput: LoginGoogleInput): LoginGoogleOutput? {
        log.info { "process loginGoogleInput: $loginGoogleInput" }

        val existSocialMember = findSocialMemberOrNull(loginGoogleInput) ?: return null
        val (accessToken, refreshToken) = createTokens(existSocialMember)

        saveRefreshToken(refreshToken)

        return LoginGoogleOutput(
            memberId = existSocialMember.member.id ?: throw MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND),
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private fun findSocialMemberOrNull(loginGoogleInput: LoginGoogleInput) =
        socialLoginRepository.findBySocialTypeAndExternalId(
            socialType = loginGoogleInput.socialType,
            externalId = loginGoogleInput.externalId,
        )

    private fun createTokens(existMember: SocialLogin): Pair<String, String> {
        val member = existMember.member

        val accessToken =
            jwtFactory.createAccessToken(
                uuid = member.uuid.toString(),
                role = member.role.name,
            )

        val refreshToken =
            jwtFactory.createRefreshToken(
                username = member.uuid.toString(),
                role = member.role.name,
            )

        return Pair(accessToken, refreshToken)
    }

    private fun saveRefreshToken(token: String) {
        val memberUuid = jwtFactory.memberUuid(token)
            ?: throw IllegalArgumentException("memberUuid is null")

        val date = Date(System.currentTimeMillis() + jwtFactory.refreshExpiredMs())

        val refreshToken =
            RefreshToken(
                memberUuid = UUID.fromString(memberUuid),
                token = token,
                expiredAt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()),
            )

        refreshTokenRepository.save(refreshToken)
    }
}
