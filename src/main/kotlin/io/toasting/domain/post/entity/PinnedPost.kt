package io.toasting.domain.post.entity

import io.toasting.domain.model.BaseEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "pinned_post",
    indexes = [
        Index(
            name = "idx_member_id_post_id",
            columnList = "member_id, post_id",
            unique = true
        )
    ]
)
class PinnedPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val post: Post,
    val memberId: Long,
) : BaseEntity() {
}