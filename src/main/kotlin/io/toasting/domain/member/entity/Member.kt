package io.toasting.domain.member.entity

import io.toasting.domain.member.vo.RoleType
import io.toasting.domain.model.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val role: RoleType,
    val profilePicture: String? = null,
    val velogId: String? = null,
    val tistoryId: String? = null,
    val nickname: String,
    val email: String,
) : BaseEntity() {
    companion object {
        fun defaultMember(
            nickname: String,
            email: String,
        ) = Member(
            role = RoleType.ROLE_USER,
            nickname = nickname,
            email = email,
        )
    }

    fun updateWith(
        nickname: String,
        email: String,
    ): Member =
        Member(
            id = id,
            role = role,
            profilePicture = profilePicture,
            velogId = velogId,
            tistoryId = tistoryId,
            nickname = nickname,
            email = email,
        )
}
