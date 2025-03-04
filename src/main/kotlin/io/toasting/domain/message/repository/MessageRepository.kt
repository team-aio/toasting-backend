package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun countByChatRoomInAndSenderIdNotAndIsRead(chatRoomList: List<ChatRoom>, senderId: Long, isRead: Boolean): Long
    fun findByChatRoomAndSenderIdNotAndIsRead(chatRoom: ChatRoom, senderId: Long, isRead: Boolean): MutableList<Message>
    fun findByChatRoom(chatRoom: ChatRoom, pageable: Pageable): Page<Message>
}
