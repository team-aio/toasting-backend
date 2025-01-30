package io.toasting.domain.post.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val sourceType: String,
    val postedAt: LocalDateTime? = null,
    val shortContent: String? = null,
    val content: String? = null,
    val title: String,
    val likeCount: Int = 0,
    val memberId: Long,
) : BaseEntity()
