package io.toasting.domain.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.PageResponse
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.post.application.PostService
import io.toasting.domain.post.controller.response.GetPostDetailResponse
import io.toasting.domain.post.controller.response.SearchPostsResponse
import io.toasting.global.api.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/posts")
@Tag(name = "Post", description = "게시글 관련 API")
internal class PostController(
    private val postService: PostService,
    private val memberUuidConverter: MemberUuidConverter,
) {
    @GetMapping("/search")
    @Operation(summary = "로그인했을 때 게시글 검색")
    fun searchPosts(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = arrayOf("postedAt"),
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
        @RequestParam("keyword", required = false) keyword: String?,
    ): ApiResponse<PageResponse<SearchPostsResponse>> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        val output = postService.searchPost(memberId, keyword, pageable)
        val response = output.content.map { SearchPostsResponse.from(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(
                response,
                output.totalElements,
                output.totalPages
            )
        )
    }

    @PostMapping("/blog/{sourceType}/{id}")
    @Operation(summary = "블로그 연동", description = "Tistory 또는 Velog를 연동합니다. id에는 각 블로그의 닉네임이 들어갑니다.")
    fun linkBlog(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("sourceType") sourceType: String,
        @PathVariable("id") id: String,
    ): ApiResponse<Unit> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        postService.linkBlog(memberId, id, sourceType)
        return ApiResponse.onSuccess()
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회", description = "리스트에서 조회된 게시글을 상세보기 합니다.")
    fun getPostDetail(
        @PathVariable("postId") postId: Long,
    ): ApiResponse<GetPostDetailResponse> {
        return ApiResponse.onSuccess(
            GetPostDetailResponse.from(
                postService.getPostDetail(postId)
            )
        )
    }
}
