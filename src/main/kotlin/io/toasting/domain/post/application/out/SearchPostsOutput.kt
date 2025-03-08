package io.toasting.domain.post.application.out

import io.toasting.domain.member.entity.Member
import io.toasting.domain.post.entity.Post
import java.time.LocalDateTime

class SearchPostsOutput(
    val id: Long,
    val sourceType: String,
    val postedAt: LocalDateTime? = null,
    val shortContent: String,
    val title: String,
    val likeCount: Int,
    val memberId: Long,
    val nickname: String,
    val profilePicture: String? = null
) {
    companion object {
        fun of(post: Post, member: Member): SearchPostsOutput {
            return SearchPostsOutput(
                id = post.id!!,
                sourceType = post.sourceType,
                postedAt = post.postedAt,
                shortContent = post.shortContent!!,
                title = post.title,
                likeCount = post.likeCount,
                memberId = member.id!!,
                nickname = member.nickname,
                profilePicture = member.profilePicture
            )
        }
    }
}