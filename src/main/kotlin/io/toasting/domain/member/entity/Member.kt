package io.toasting.domain.member.entity

import io.toasting.domain.member.vo.RoleType
import io.toasting.domain.model.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(
    name = "member",
    indexes = [
        Index(
            name = "idx_member_uuid",
            columnList = "uuid",
            unique = true
        )
    ]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "uuid")
    val uuid: UUID,
    @Enumerated(EnumType.STRING)
    val role: RoleType,
    val profilePicture: String? = null,
    var velogId: String? = null,
    var tistoryId: String? = null,
    val nickname: String,
    val email: String,
) : BaseEntity() {
    companion object {
        fun defaultMember(
            nickname: String,
            email: String,
            uuid: UUID
        ) = Member(
            role = RoleType.ROLE_USER,
            nickname = nickname,
            email = email,
            uuid = uuid,
        )
    }

    fun registerBlog(sourceType: String, id: String) {
        if (sourceType == "tistory") {
            this.tistoryId = id
        } else {
            this.velogId = id
        }
    }
}
