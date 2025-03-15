package io.toasting.domain.member.application.input

data class ExistsFollowInput(
    val fromMemberId: Long,
    val toMemberId: Long,
)
