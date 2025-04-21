package io.toasting.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.domain.member.application.FollowService
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.application.input.CancelFollowInput
import io.toasting.domain.member.application.input.ExistsFollowInput
import io.toasting.domain.member.controller.response.ExistsFollowResponse
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.global.api.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/follow")
@Tag(name = "Following", description = "팔로우 관련 API")
class FollowController(
    private val followService: FollowService,
    private val memberUuidConverter: MemberUuidConverter
) {
    @PostMapping("/{memberId}")
    @Operation(summary = "팔로우 추가", description = "해당 사용자를 팔로우합니다.")
    fun addFollow(
        @PathVariable memberId: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val fromMemberId = memberUuidConverter.toMemberId(UUID.fromString(memberDetails.username))
        val toMemberId = memberUuidConverter.toMemberId(UUID.fromString(memberId))

        val addFollowInput = AddFollowInput(fromMemberId = fromMemberId, toMemberId = toMemberId)

        followService.addFollow(addFollowInput)
        return ApiResponse.onSuccess()
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "팔로우 취소", description = "해당 사용자를 팔로우를 취소합니다.")
    fun cancelFollow(
        @PathVariable memberId: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val fromMemberId = memberUuidConverter.toMemberId(UUID.fromString(memberDetails.username))
        val toMemberId = memberUuidConverter.toMemberId(UUID.fromString(memberId))

        val cancelFollowInput = CancelFollowInput(fromMemberId = fromMemberId, toMemberId = toMemberId)

        followService.cancelFollow(cancelFollowInput)
        return ApiResponse.onSuccess()
    }

    @GetMapping("/{memberId}/exist")
    @Operation(summary = "팔로우 여부 확인", description = "해당 사용자를 팔로우 했는지 확인합니다. 이미 했다면 true를 반환합니다.")
    fun isExistFollow(
        @PathVariable memberId: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<ExistsFollowResponse> {
        val fromMemberId = memberUuidConverter.toMemberId(UUID.fromString(memberDetails.username))
        val toMemberId = memberUuidConverter.toMemberId(UUID.fromString(memberId))
        
        val existsFollowInput = ExistsFollowInput(fromMemberId = fromMemberId, toMemberId = toMemberId)

        return followService
            .existsFollow(existsFollowInput)
            .let { isExist -> ExistsFollowResponse(isExist = isExist) }
            .let { ApiResponse.onSuccess(it) }
    }
}
