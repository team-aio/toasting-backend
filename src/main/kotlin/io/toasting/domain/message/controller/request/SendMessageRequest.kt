package io.toasting.domain.message.controller.request

import io.toasting.domain.message.applicatoin.input.SendMessageInput
import jakarta.validation.constraints.NotBlank

data class SendMessageRequest(
    @field:NotBlank(message = "content cannot be null or empty")
    val content: String,
) {
    fun toInput(): SendMessageInput {
        return SendMessageInput(
            content = content,
        )
    }
}
