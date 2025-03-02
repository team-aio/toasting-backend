package io.toasting.domain.message.controller.response

import io.toasting.domain.message.applicatoin.out.GetMessageCountOutput

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
