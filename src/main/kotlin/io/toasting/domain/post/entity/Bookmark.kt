package io.toasting.domain.post.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.*


@Table(name = "bookmark")
@Entity
class Bookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,
    val memberId: Long,
): BaseEntity()