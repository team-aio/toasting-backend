package io.toasting.domain.post.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "pinned_post")
class PinnedPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val post: Post,
    val memberId: Long,
) : BaseEntity() {

}