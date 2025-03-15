package io.toasting.domain.post.controller.response

import io.toasting.domain.post.application.out.GetPostDetailOutput
import java.time.LocalDateTime

class GetPostDetailResponse(
    val id: Long,
    val sourceType: String,
    val postedAt: LocalDateTime? = null,
    val content: String,
    val url: String,
    val title: String,
    val likeCount: Int,
    val memberId: Long,
    val nickname: String,
    val profilePicture: String? = null
) {
    companion object {
        fun from(output: GetPostDetailOutput) = GetPostDetailResponse(
            id = output.id,
            sourceType = output.sourceType,
            postedAt = output.postedAt,
            content = output.content,
            url = output.url,
            title = output.title,
            likeCount = output.likeCount,
            memberId = output.memberId,
            nickname = output.nickname,
            profilePicture = output.profilePicture
        )
    }
}