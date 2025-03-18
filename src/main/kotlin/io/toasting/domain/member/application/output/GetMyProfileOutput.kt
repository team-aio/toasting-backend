package io.toasting.domain.member.application.output

data class GetMyProfileOutput(
    val nickname: String,
    val followingCount: Long,
    val followerCount: Long,
    val postCount: Long,
    val velogId: String?,
    val tistoryId: String?,
)
