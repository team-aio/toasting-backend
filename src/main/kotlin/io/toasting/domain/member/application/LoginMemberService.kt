package io.toasting.domain.member.application

import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.domain.member.application.input.LoginGoogleInput
import io.toasting.domain.member.application.output.LoginGoogleOutput
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.RefreshToken
import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.repository.RefreshTokenRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import io.toasting.global.security.jwt.JwtFactory
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

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

        val existSocialMember = tryFindSocialMember(loginGoogleInput)

        if (existSocialMember == null) {
            log.info { "create New Member" }
            val newSocialMember = createNewSocialMember(loginGoogleInput)
            socialLoginRepository.save(newSocialMember)
            return null
        }

        log.info { "exist Member: $existSocialMember" }

        val (accessToken, refreshToken) = createTokens(existSocialMember)

        saveRefreshToken(refreshToken)

        return LoginGoogleOutput(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private fun createNewSocialMember(loginGoogleInput: LoginGoogleInput) =
        SocialLogin(
            socialType = loginGoogleInput.socialType,
            externalId = loginGoogleInput.externalId,
            member =
                Member.defaultMember(
                    email = loginGoogleInput.email,
                    nickname = loginGoogleInput.username,
                ),
        )

    private fun tryFindSocialMember(loginGoogleInput: LoginGoogleInput) =
        socialLoginRepository.findBySocialTypeAndExternalId(
            socialType = loginGoogleInput.socialType,
            externalId = loginGoogleInput.externalId,
        )

    private fun createTokens(existMember: SocialLogin): Pair<String, String> {
        val member = existMember.member

        val accessToken =
            jwtFactory.createAccessToken(
                username = member.id.toString(),
                role = member.role.name,
            )

        val refreshToken =
            jwtFactory.createRefreshToken(
                username = member.id.toString(),
                role = member.role.name,
            )

        return Pair(accessToken, refreshToken)
    }

    private fun saveRefreshToken(token: String) {
        val memberId = jwtFactory.memberId(token) ?: throw IllegalArgumentException("memberId is null")
        val date = Date(System.currentTimeMillis() + jwtFactory.refreshExpiredMs())

        val refreshToken =
            RefreshToken(
                memberId = memberId.toLong(),
                token = token,
                expiredAt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()),
            )

        refreshTokenRepository.save(refreshToken)
    }
}
