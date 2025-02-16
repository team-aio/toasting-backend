package io.toasting.domain.member.controller.response

data class LoginGoogleResponse(
    val accessToken: String,
    val refreshToken: String,
)
