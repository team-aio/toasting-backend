package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.Follow
import io.toasting.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository : JpaRepository<Follow, Long> {
    fun deleteByFromMemberAndToMember(
        fromMember: Member,
        toMember: Member,
    )
}
