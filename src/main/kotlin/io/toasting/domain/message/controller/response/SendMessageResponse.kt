package io.toasting.domain.message.controller.response

import io.toasting.domain.message.applicatoin.out.SendMessageOutput
import java.time.LocalDateTime

class SendMessageResponse(
    val id: Long,
    val postId: Long?,
    val receiverId: Long,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun mock() =
            SendMessageResponse(
                id = 1L,
                postId = 2L,
                receiverId = 3L,
                content = "Hello World",
                createdAt = LocalDateTime.now(),
            )

        fun fromOutput(output: SendMessageOutput): SendMessageResponse {
            return SendMessageResponse(
                id = output.id,
                postId = output.postId,
                receiverId = output.receiverId,
                content = output.content,
                createdAt = output.createdAt,
            )
        }
    }
}
