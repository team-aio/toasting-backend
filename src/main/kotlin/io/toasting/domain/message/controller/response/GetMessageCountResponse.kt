package io.toasting.domain.message.controller.response

import io.toasting.domain.message.applicatoin.output.GetMessageCountOutput

class GetMessageCountResponse(
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
