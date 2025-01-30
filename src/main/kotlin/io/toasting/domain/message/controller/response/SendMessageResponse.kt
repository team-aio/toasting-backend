package io.toasting.domain.message.controller.response

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
    }
}
