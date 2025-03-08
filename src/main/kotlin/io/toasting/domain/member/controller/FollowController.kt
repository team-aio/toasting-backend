package io.toasting.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.domain.member.application.FollowService
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.application.input.CancelFollowInput
import io.toasting.domain.member.controller.response.ExistsFollowingResponse
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.global.api.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/follow")
@Tag(name = "Following", description = "팔로우 관련 API")
class FollowController(
    private val followService: FollowService,
) {
    @GetMapping("/{followingId}/exists")
    fun existsFollowing(
        @PathVariable followingId: Long,
    ): ApiResponse<ExistsFollowingResponse> = ApiResponse.onSuccess(ExistsFollowingResponse.mock())

    @PostMapping("/{memberId}")
    @Operation(summary = "팔로우 추가", description = "해당 사용자를 팔로우합니다.")
    fun addFollow(
        @PathVariable memberId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val fromMemberId = memberDetails.username.toLong()
        val addFollowInput = AddFollowInput(fromMemberId = fromMemberId, toMemberId = memberId)

        followService.addFollow(addFollowInput)
        return ApiResponse.onSuccess()
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "팔로우 취소", description = "해당 사용자를 팔로우를 취소합니다.")
    fun cancelFollow(
        @PathVariable memberId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val fromMemberId = memberDetails.username.toLong()
        val cancelFollowInput = CancelFollowInput(fromMemberId = fromMemberId, toMemberId = memberId)

        followService.cancelFollow(cancelFollowInput)
        return ApiResponse.onSuccess()
    }
}
