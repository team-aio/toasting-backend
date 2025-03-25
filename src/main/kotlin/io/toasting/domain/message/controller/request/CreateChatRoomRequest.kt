package io.toasting.domain.message.controller.request

import io.toasting.domain.message.applicatoin.input.CreateChatRoomInput
import jakarta.validation.constraints.NotNull

class CreateChatRoomRequest(
    @field:NotNull(message = "partnerId cannot be null or empty")
    val partnerId: Long
) {
    fun toInput(): CreateChatRoomInput = CreateChatRoomInput(
            partnerId = partnerId
        )
}