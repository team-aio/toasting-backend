package io.toasting.domain.post.repository

import io.toasting.domain.post.entity.PinnedPost
import org.springframework.data.jpa.repository.JpaRepository

interface PinnedPostRepository : JpaRepository<PinnedPost, Long> {
}