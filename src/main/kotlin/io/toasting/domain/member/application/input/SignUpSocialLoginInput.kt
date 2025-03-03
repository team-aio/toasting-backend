package io.toasting.domain.member.application.input

import io.toasting.domain.member.vo.SocialType

data class SignUpSocialLoginInput(
    val email: String,
    val username: String,
    val nickname: String,
    val socialType: SocialType,
    val externalId: String,
    val tistoryId: String?,
    val velogId: String?,
)
