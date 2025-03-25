package io.toasting.domain.message.controller.response

import io.toasting.domain.message.applicatoin.out.CreateChatRoomOutput
import java.time.LocalDateTime

class CreateChatRoomResponse(
    val chatRoomId: Long,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(output: CreateChatRoomOutput) = CreateChatRoomResponse(
            chatRoomId = output.chatRoomId,
            createdAt = output.createdAt
        )
    }
}