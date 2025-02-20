package io.toasting.domain.member.application.output

data class LoginGoogleOutput(
    val accessToken: String,
    val refreshToken: String,
)
