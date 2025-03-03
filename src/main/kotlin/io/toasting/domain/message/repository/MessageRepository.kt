package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun countByChatRoomInAndSenderIdNotAndIsRead(chatRoomList: List<ChatRoom>, senderId: Long, isRead: Boolean): Long
    fun findByChatRoomAndSenderIdNotAndIsRead(chatRoom: ChatRoom, senderId: Long, isRead: Boolean): MutableList<Message>
}
