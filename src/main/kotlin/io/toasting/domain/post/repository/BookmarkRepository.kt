package io.toasting.domain.post.repository

import io.toasting.domain.post.entity.Bookmark
import io.toasting.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findByPostInAndMemberId(postList: List<Post>, memberId: Long): List<Bookmark>
    fun deleteAllByMemberId(memberId: Long): Long
}