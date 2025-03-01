package io.toasting.domain.member.application

import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.domain.member.application.input.LoginGoogleInput
import io.toasting.domain.member.application.output.LoginGoogleOutput
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.repository.SocialLoginRepository
import io.toasting.global.security.jwt.JwtFactory
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class LoginMemberService(
    private val socialLoginRepository: SocialLoginRepository,
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
        val accessToken =
            jwtFactory.createAccessToken(
                username = existMember.member.id.toString(),
                role = "ROLE_USER",
            )

        val refreshToken =
            jwtFactory.createRefreshToken(
                username = existMember.member.id.toString(),
                role = "ROLE_USER",
            )
        return Pair(accessToken, refreshToken)
    }
}
