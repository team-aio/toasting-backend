package io.toasting.creator.post

import io.toasting.domain.post.entity.Bookmark
import io.toasting.domain.post.entity.Post

class BookmarkCreator {
    companion object {
        fun default(post: Post, memberId: Long) = Bookmark(post = post, memberId = memberId)
    }
}