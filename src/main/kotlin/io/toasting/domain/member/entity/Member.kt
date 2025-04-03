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

@Entity
@Table(name = "member", indexes = [Index(name = "idx_member_id_hash", columnList = "member_id_hash")])
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val role: RoleType,
    val profilePicture: String? = null,
    var velogId: String? = null,
    var tistoryId: String? = null,
    val nickname: String,
    val email: String,
    @Column(name = "member_id_hash")
    val memberIdHash: String,
) : BaseEntity() {
    companion object {
        fun defaultMember(
            nickname: String,
            email: String,
        ) = Member(
            role = RoleType.ROLE_USER,
            nickname = nickname,
            email = email,
            memberIdHash = "hash",
        )
    }

    fun registerBlog(
        sourceType: String,
        id: String,
    ) {
        if (sourceType.equals("tistory")) {
            this.tistoryId = id
        } else {
            this.velogId = id
        }
    }
}
