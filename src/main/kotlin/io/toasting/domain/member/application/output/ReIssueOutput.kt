package io.toasting.domain.member.application.output

data class ReIssueOutput(
    val newAccessToken: String,
    val newRefreshToken: String,
)
