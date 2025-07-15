package io.toasting.domain.post.controller

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

    @PostMapping("/{postId}/pin")
    fun pinPost(
        @PathVariable("postId") postId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        pinPostService.pinPost(memberId, postId)
        return ApiResponse.onSuccess()
    }

    @DeleteMapping("/{postId}/pin")
    fun unpinPost(
        @PathVariable("postId") postId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        pinPostService.unpinPost(memberId, postId)
        return ApiResponse.onSuccess()
    }

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