package io.toasting.domain.member.application.input

data class CancelFollowInput(
    val fromMemberId: Long,
    val toMemberId: Long,
)
