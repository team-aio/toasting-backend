package io.toasting.domain.member.controller.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.toasting.domain.member.application.output.GetProfileOutput

@JsonInclude(JsonInclude.Include.NON_NULL) // NULL인 값은 포함시키지 않기 위함
data class GetProfileResponse(
    val nickname: String,
    val followingCount: Long,
    val followerCount: Long,
    val postCount: Long,
    val velogId: String?,
    val tistoryId: String?,
) {
    companion object {
        fun from(getMyProfileOutput: GetProfileOutput) =
            GetProfileResponse(
                nickname = getMyProfileOutput.nickname,
                followerCount = getMyProfileOutput.followerCount,
                followingCount = getMyProfileOutput.followerCount,
                postCount = getMyProfileOutput.postCount,
                velogId = getMyProfileOutput.velogId,
                tistoryId = getMyProfileOutput.tistoryId,
            )
    }
}
