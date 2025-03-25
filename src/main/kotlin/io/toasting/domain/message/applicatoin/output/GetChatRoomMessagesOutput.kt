package io.toasting.domain.message.applicatoin.output

import io.toasting.domain.message.entity.Message
import java.time.LocalDateTime

class GetChatRoomMessagesOutput(
    val id: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(message: Message): GetChatRoomMessagesOutput {
            return GetChatRoomMessagesOutput(
                id = message.id!!,
                chatRoomId = message.chatRoom.id!!,
                senderId = message.senderId,
                content = message.content,
                createdAt = message.createdAt
            )
        }
    }
}