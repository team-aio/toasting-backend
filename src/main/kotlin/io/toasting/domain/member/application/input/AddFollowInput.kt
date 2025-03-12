package io.toasting.domain.member.application.input

data class AddFollowInput(
    val fromMemberId: Long,
    val toMemberId: Long,
)
