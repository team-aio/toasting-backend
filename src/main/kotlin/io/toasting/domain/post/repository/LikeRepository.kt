package io.toasting.domain.post.repository

import io.toasting.domain.post.entity.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository : JpaRepository<Like, Long> {
    fun deleteAllByMemberId(memberId: Long): Long
}
