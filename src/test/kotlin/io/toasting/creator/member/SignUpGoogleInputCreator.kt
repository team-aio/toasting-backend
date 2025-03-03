package io.toasting.creator.member

import io.toasting.domain.member.application.input.SignUpSocialLoginInput
import io.toasting.domain.member.vo.SocialType

class SignUpGoogleInputCreator {
    companion object {
        fun default(
            email: String,
            username: String,
            nickname: String,
            externalId: String,
        ) = SignUpSocialLoginInput(
            email = email,
            username = username,
            nickname = nickname,
            socialType = SocialType.GOOGLE,
            externalId = externalId,
            tistoryId = null,
            velogId = null,
        )
    }
}
