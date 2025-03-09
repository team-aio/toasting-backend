package io.toasting.domain.member.application.input

data class CheckFollowInput(
    val fromMemberId: Long,
    val toMemberId: Long,
)
