package io.toasting.domain.message.applicatoin.out

import io.toasting.domain.message.entity.Message
import java.time.LocalDateTime

class SendMessageOutput(
    val id: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(message: Message): SendMessageOutput {
            return SendMessageOutput(
                id = message.id!!,
                chatRoomId = message.chatRoom.id!!,
                senderId = message.senderId,
                content = message.content,
                createdAt = message.createdAt,
            )
        }
    }
}