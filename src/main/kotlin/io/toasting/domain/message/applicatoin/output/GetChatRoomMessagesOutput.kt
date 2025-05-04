package io.toasting.domain.message.applicatoin.output

import io.toasting.domain.message.entity.Message
import java.time.LocalDateTime
import java.util.UUID

class GetChatRoomMessagesOutput(
    val id: Long,
    val chatRoomId: Long,
    val senderId: String,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(message: Message, uuid: UUID): GetChatRoomMessagesOutput {
            return GetChatRoomMessagesOutput(
                id = message.id!!,
                chatRoomId = message.chatRoom.id!!,
                senderId = uuid.toString(),
                content = message.content,
                createdAt = message.createdAt
            )
        }
    }
}