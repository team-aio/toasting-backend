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

    fun existsByFromMemberAndToMember(
        fromMember: Member,
        toMember: Member,
    ): Boolean

    /*
     * 내가 팔로잉 한 사람의 수 (팔로잉 수)
     */
    fun countByFromMember(fromMember: Member): Long

    /*
     * 나를 팔로잉 한 사람의 수 (팔로워 수)
     */
    fun countByToMember(toMember: Member): Long
}
