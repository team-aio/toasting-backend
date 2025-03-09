package io.toasting.domain.member.entity

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.exception.FollowExceptionHandler
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Follow(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    val fromMember: Member,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    val toMember: Member,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    init {
        validate()
    }

    private fun validate() {
        if (fromMember == toMember) {
            throw FollowExceptionHandler.SelfFollowException(ErrorStatus.SELF_FOLLOW_BANNED)
        }
    }
}
