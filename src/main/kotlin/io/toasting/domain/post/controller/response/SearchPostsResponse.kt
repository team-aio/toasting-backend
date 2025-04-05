package io.toasting.domain.post.controller.response

import io.toasting.domain.post.application.out.SearchPostsOutput
import java.time.LocalDateTime

class SearchPostsResponse(
    val id: Long,
    val sourceType: String,
    val postedAt: LocalDateTime? = null,
    val shortContent: String,
    val title: String,
    val likeCount: Int,
    val memberId: Long,
    val nickname: String,
    val profilePicture: String? = null,
    val isBookmarked: Boolean,
) {
    companion object {
        fun from(output: SearchPostsOutput): SearchPostsResponse {
            return SearchPostsResponse(
                id = output.id,
                sourceType = output.sourceType,
                postedAt = output.postedAt,
                shortContent = output.shortContent,
                title = output.title,
                likeCount = output.likeCount,
                memberId = output.memberId,
                nickname = output.nickname,
                profilePicture = output.profilePicture,
                isBookmarked = output.isBookmarked
            )
        }
    }
}
