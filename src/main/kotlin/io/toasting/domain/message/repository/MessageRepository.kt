package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun countByChatRoomInAndIsRead(chatRoomList: List<ChatRoom>, isRead: Boolean): Long
    fun findByChatRoomAndSenderIdNotAndIsRead(chatRoom: ChatRoom, senderId: Long, isRead: Boolean): MutableList<Message>
}
