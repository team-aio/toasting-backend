package io.toasting.domain.message.applicatoin.output

import io.toasting.domain.message.entity.ChatRoom
import java.time.LocalDateTime

class CreateChatRoomOutput(
    val chatRoomId: Long,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(chatRoom: ChatRoom) = CreateChatRoomOutput(
                chatRoomId = chatRoom.id!!,
                createdAt = chatRoom.createdAt,
            )
    }
}