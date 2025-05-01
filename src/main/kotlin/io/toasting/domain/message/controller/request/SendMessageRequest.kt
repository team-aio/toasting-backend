package io.toasting.domain.message.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.message.applicatoin.input.SendMessageInput
import jakarta.validation.constraints.NotBlank

data class SendMessageRequest(
    @field:NotBlank(message = "content cannot be null or empty")
    @Schema(description = "메세지 내용", example = "안녕하세요.")
    val content: String,
) {
    fun toInput(): SendMessageInput {
        return SendMessageInput(
            content = content,
        )
    }
}
