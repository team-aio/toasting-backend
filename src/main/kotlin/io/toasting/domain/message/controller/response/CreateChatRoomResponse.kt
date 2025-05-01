package io.toasting.domain.message.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.message.applicatoin.output.CreateChatRoomOutput
import java.time.LocalDateTime

class CreateChatRoomResponse(
    @Schema(description = "채팅방 id", example = "1")
    val chatRoomId: Long,
    @Schema(description = "생성 시간", example = "2025-04-27T07:50:26.255Z")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(output: CreateChatRoomOutput) = CreateChatRoomResponse(
            chatRoomId = output.chatRoomId,
            createdAt = output.createdAt
        )
    }
}