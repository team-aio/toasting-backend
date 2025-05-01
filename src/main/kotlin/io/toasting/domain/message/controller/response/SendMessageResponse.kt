package io.toasting.domain.message.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.message.applicatoin.output.SendMessageOutput
import java.time.LocalDateTime

class SendMessageResponse(
    @Schema(description = "메세지 id", example = "1")
    val id: Long,
    @Schema(description = "채팅방 id", example = "1")
    val chatRoomId: Long,
    @Schema(description = "발신자 id", example = "1")
    val senderId: Long,
    @Schema(description = "메세지 내용", example = "안녕하세요.")
    val content: String,
    @Schema(description = "보낸 시간", example = "2025-04-27T07:50:26.255Z")
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
