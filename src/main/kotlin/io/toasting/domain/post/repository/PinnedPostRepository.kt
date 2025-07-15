package io.toasting.domain.post.repository

import io.toasting.domain.post.entity.PinnedPost
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PinnedPostRepository : JpaRepository<PinnedPost, Long> {
    fun findByPostIdAndMemberId(postId: Long, memberId: Long): Optional<PinnedPost>
    fun findByMemberId(memberId: Long): List<PinnedPost>
}