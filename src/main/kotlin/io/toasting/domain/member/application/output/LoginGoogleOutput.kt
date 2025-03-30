package io.toasting.domain.member.application.output

data class LoginGoogleOutput(
    val memberId: Long,
    val accessToken: String,
    val refreshToken: String,
)
