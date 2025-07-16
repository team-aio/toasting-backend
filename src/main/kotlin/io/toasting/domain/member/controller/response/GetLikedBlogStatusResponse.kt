package io.toasting.domain.member.controller.response

import io.toasting.domain.member.application.output.GetLinkedBlogStatusOutput

class GetLikedBlogStatusResponse(
    val velogId: String?,
    val tistoryId: String?,
) {
    companion object {
        fun from(output: GetLinkedBlogStatusOutput) = GetLikedBlogStatusResponse(
            velogId = output.velogId,
            tistoryId = output.tistoryId,
        )
    }
}