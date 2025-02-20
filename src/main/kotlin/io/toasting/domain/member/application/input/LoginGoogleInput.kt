package io.toasting.domain.member.application.input

import io.toasting.domain.member.vo.SocialType

data class LoginGoogleInput(
    val email: String,
    val username: String,
    val socialType: SocialType,
    val externalId: String,
)
