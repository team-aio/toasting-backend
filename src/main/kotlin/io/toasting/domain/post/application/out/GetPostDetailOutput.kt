package io.toasting.domain.post.application.out

import io.toasting.domain.member.entity.Member
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.vo.SourceType
import java.time.LocalDateTime

class GetPostDetailOutput(
    val id: Long,
    val sourceType: SourceType,
    val url: String,
    val postedAt: LocalDateTime? = null,
    val content: String,
    val title: String,
    val likeCount: Int,
    val memberId: Long,
    val nickname: String,
    val profilePicture: String? = null
) {
    companion object {
        fun of(post: Post, member: Member): GetPostDetailOutput =
            GetPostDetailOutput(
                id = post.id!!,
                sourceType = post.sourceType,
                url = post.url,
                postedAt = post.postedAt,
                content = post.content!!,
                title = post.title,
                likeCount = post.likeCount,
                memberId = member.id!!,
                nickname = member.nickname,
                profilePicture = member.profilePicture
            )
    }
}