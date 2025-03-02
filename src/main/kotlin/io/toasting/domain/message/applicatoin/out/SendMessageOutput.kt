package io.toasting.domain.message.applicatoin.out

import io.toasting.domain.message.entity.Message
import java.time.LocalDateTime

class SendMessageOutput(
    val id: Long,
    val postId: Long?,
    val receiverId: Long,
    val content: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(message: Message): SendMessageOutput {
            return SendMessageOutput(
                id = message.id!!,
                postId = message.postId,
                receiverId = message.receiverId,
                content = message.content,
                createdAt = message.createdAt,
            )
        }
    }
}