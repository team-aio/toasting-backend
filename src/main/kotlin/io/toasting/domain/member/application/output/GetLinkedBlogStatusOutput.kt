package io.toasting.domain.member.application.output

import io.toasting.domain.member.entity.Member

class GetLinkedBlogStatusOutput(
    val velogId: String?,
    val tistoryId: String?,
) {
    companion object {
        fun from(member: Member) = GetLinkedBlogStatusOutput(
            velogId = member.velogId,
            tistoryId = member.tistoryId,
        )
    }
}