package io.toasting.domain.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.post.application.GetPinnedPostService
import io.toasting.domain.post.application.PinPostService
import io.toasting.domain.post.controller.response.GetPinnedPostResponse
import io.toasting.global.api.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/v1/posts")
@Tag(name = "PinPost", description = "고정 게시글 관련 API")
class PinPostController(
    private val memberUuidConverter: MemberUuidConverter,
    private val pinPostService: PinPostService,
    private val getPinnedPostService: GetPinnedPostService,
    ) {

    @Operation(summary = "게시글 고정", description = "나의 게시글 중, 고절할 게시글을 추가합니다.")
    @PostMapping("/{postId}/pin")
    fun pinPost(
        @PathVariable("postId") postId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        pinPostService.pinPost(memberId, postId)
        return ApiResponse.onSuccess()
    }

    @Operation(summary = "게시글 고정 해제", description = "고정한 게시글을 해제합니다.")
    @DeleteMapping("/{postId}/pin")
    fun unpinPost(
        @PathVariable("postId") postId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        pinPostService.unpinPost(memberId, postId)
        return ApiResponse.onSuccess()
    }

    @Operation(summary = "고정 게시글 리스트 조회", description = "writerId(유저)의 고정 게시글 리스트를 조회합니다.")
    @GetMapping("/pin")
    fun getPinnedPosts(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestParam("writerId") writerId: String,
    ): ApiResponse<List<GetPinnedPostResponse>> {
        val writerId = memberUuidConverter.toMemberId(writerId)
        val output = if (memberDetails == null) {
            getPinnedPostService.getPinnedPostListWithoutLogin(writerId)
        } else {
            val memberId = memberUuidConverter.toMemberId(memberDetails.username)
            getPinnedPostService.getPinnedPostListWithLogin(memberId, writerId)
        }

        return ApiResponse.onSuccess(
            output.map { GetPinnedPostResponse.from(it) }
        )
    }
}