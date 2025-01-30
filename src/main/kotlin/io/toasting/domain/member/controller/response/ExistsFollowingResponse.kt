package io.toasting.domain.member.controller.response

class ExistsFollowingResponse(
    val isExists: Boolean,
) {
    companion object {
        fun mock() =
            ExistsFollowingResponse(
                isExists = false,
            )
    }
}
