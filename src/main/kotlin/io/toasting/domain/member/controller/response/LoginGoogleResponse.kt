package io.toasting.domain.member.controller.response

import io.toasting.domain.member.application.output.LoginGoogleOutput

data class LoginGoogleResponse(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun from(loginGoogleOutput: LoginGoogleOutput) =
            LoginGoogleResponse(
                accessToken = loginGoogleOutput.accessToken,
                refreshToken = loginGoogleOutput.refreshToken,
            )
    }
}
