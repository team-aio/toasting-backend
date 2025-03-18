package io.toasting.domain.post.repository

import io.toasting.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository :
    JpaRepository<Post, Long>,
    CustomPostRepository {
    fun countByMemberId(memberId: Long): Long
}
