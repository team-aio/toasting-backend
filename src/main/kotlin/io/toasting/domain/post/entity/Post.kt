package io.toasting.domain.post.entity

import io.toasting.domain.model.BaseEntity
import io.toasting.domain.post.vo.SourceType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val sourceType: SourceType,
    val url: String,
    val postedAt: LocalDateTime? = null,
    val shortContent: String? = null,
    @Column(columnDefinition = "text")
    val content: String? = null,
    val title: String,
    val likeCount: Int = 0,
    val memberId: Long,
) : BaseEntity()
