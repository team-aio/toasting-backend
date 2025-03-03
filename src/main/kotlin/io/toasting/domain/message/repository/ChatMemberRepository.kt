package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.ChatMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember, Long> {
}