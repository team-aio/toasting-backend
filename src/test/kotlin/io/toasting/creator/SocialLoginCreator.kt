package io.toasting.creator

import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.vo.SocialType

class SocialLoginCreator {
    companion object {
        fun defaultGoogleLogin(
            externalId: String,
            member: Member,
        ) = SocialLogin(
            socialType = SocialType.GOOGLE,
            externalId = externalId,
            member = member,
        )

        fun googleLoginIgnoreMember(externalId: String) {
            SocialLogin(
                socialType = SocialType.GOOGLE,
                externalId = externalId,
                member = Member.defaultMember("test", "test@test.com"),
            )
        }
    }
}
