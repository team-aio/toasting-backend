package io.toasting.creator.post

import io.toasting.domain.post.entity.Like
import io.toasting.domain.post.entity.Post

class LikeCreator {
    companion object {
        fun default(post: Post, memberId: Long) = Like(post = post, memberId = memberId)
    }
}