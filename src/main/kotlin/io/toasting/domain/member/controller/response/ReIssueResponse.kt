package io.toasting.domain.member.controller.response

import io.toasting.domain.member.application.output.ReIssueOutput

data class ReIssueResponse(
    val newAccessToken: String,
    val newRefreshToken: String,
) {
    companion object {
        fun from(reIssueOutput: ReIssueOutput) =
            ReIssueResponse(
                newAccessToken = reIssueOutput.newAccessToken,
                newRefreshToken = reIssueOutput.newRefreshToken,
            )
    }
}
