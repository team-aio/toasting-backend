package io.toasting.domain.member.application.output

import io.toasting.domain.member.entity.Member

data class GetProfileOutput(
    val nickname: String,
    val followingCount: Long,
    val followerCount: Long,
    val postCount: Long,
    val velogId: String?,
    val tistoryId: String?,
) {
    companion object {
        fun of(
            member: Member,
            followingCount: Long,
            followerCount: Long,
            postCount: Long,
        ) = GetProfileOutput(
            nickname = member.nickname,
            followerCount = followerCount,
            followingCount = followingCount,
            postCount = postCount,
            velogId = member.velogId,
            tistoryId = member.tistoryId,
        )
    }
}
