package io.toasting.domain.message.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.message.applicatoin.output.GetMessageCountOutput

class GetMessageCountResponse(
    @Schema(description = "읽지 않은 메세지 개수", example = "10")
    val count: Long,
) {
    companion object {
        fun mock() =
            GetMessageCountResponse(
                count = 10,
            )

        fun fromOutput(output: GetMessageCountOutput) = GetMessageCountResponse(
            count = output.count
        )
    }
}
