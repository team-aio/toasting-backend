package io.toasting.domain.message.controller.request

import io.toasting.domain.message.applicatoin.`in`.SendMessageInput
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SendMessageRequest(
    @field:NotNull(message = "receiverId cannot be null")
    val receiverId: Long,
    val postId: Long?,
    @field:NotBlank(message = "content cannot be null or empty")
    val content: String,
) {
    fun toInput(): SendMessageInput {
        return SendMessageInput(
            receiverId = receiverId,
            postId = postId,
            content = content,
        )
    }
}
