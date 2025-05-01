package io.toasting.domain.post.repository

import io.toasting.domain.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface CustomPostRepository {
    fun searchByKeyword(keyword: String?, pageable: Pageable): Page<Post>
    fun deleteAllByMemberId(memberId: Long): Long
}