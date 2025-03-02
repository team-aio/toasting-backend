package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun countByReceiverIdAndIsRead(receiverId: Long, isRead: Boolean): Long
}
