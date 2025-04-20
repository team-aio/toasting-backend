package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByNickname(nickname: String): Boolean
    fun existsByUuid(uuid: UUID): Boolean
}
