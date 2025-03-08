package io.toasting.creator.member

import io.toasting.domain.post.entity.Post
import java.time.LocalDate
import java.time.LocalDateTime

class PostCreator {
    companion object {
        fun defaultPost(
            title: String,
            content: String,
            shortContent: String,
            memberId: Long,
            postedAt: LocalDateTime,
        ): Post {
            return Post(
                title = title,
                content = content,
                shortContent = shortContent,
                sourceType = "TISTORY",
                postedAt = postedAt,
                likeCount = 0,
                memberId = memberId
            )
        }
    }
}