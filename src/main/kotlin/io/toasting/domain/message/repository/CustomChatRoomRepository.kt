package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.ChatRoom
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface CustomChatRoomRepository {
    fun findByMemberId(memberId: Long, pageable: Pageable): Page<ChatRoom>
}