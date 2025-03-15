package io.toasting.domain.member.controller.response

class ExistsFollowResponse(
    val isExist: Boolean,
) {
    companion object {
        fun mock() =
            ExistsFollowResponse(
                isExist = false,
            )
    }
}
