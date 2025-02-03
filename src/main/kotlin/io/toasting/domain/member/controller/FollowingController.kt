package io.toasting.domain.member.controller

import io.toasting.domain.member.controller.response.ExistsFollowingResponse
import io.toasting.global.api.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/following")
class FollowingController {
    @GetMapping("/{followingId}/exists")
    fun existsFollowing(
        @PathVariable followingId: Long,
    ): ApiResponse<ExistsFollowingResponse> = ApiResponse.onSuccess(ExistsFollowingResponse.mock())

    @PostMapping("/{followingId}")
    fun following(
        @PathVariable followingId: Long,
    ): ApiResponse<Unit> = ApiResponse.onSuccess()
}
