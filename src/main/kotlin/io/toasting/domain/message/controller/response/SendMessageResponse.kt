package io.toasting.domain.message.controller.response

import io.toasting.domain.message.applicatoin.out.SendMessageOutput
import java.time.LocalDateTime

class SendMessageResponse(
    val id: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromOutput(output: SendMessageOutput): SendMessageResponse {
            return SendMessageResponse(
                id = output.id,
                chatRoomId = output.chatRoomId,
                senderId = output.senderId,
                content = output.content,
                createdAt = output.createdAt,
            )
        }
    }
}
