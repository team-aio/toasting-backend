package io.toasting.domain.message.controller.response

import io.toasting.domain.message.applicatoin.out.GetChatRoomMessagesOutput
import java.time.LocalDateTime

class GetChatRoomMessagesResponse(
    val id: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromOutput(output: GetChatRoomMessagesOutput): GetChatRoomMessagesResponse {
            return GetChatRoomMessagesResponse(
                id = output.id,
                chatRoomId = output.chatRoomId,
                senderId = output.senderId,
                content = output.content,
                createdAt = output.createdAt,
            )
        }
    }
}