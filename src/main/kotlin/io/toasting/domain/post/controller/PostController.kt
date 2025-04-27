package io.toasting.domain.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.PageResponse
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.post.application.PostService
import io.toasting.domain.post.controller.response.GetPostDetailResponse
import io.toasting.domain.post.controller.response.SearchPostsResponse
import io.toasting.domain.post.vo.SourceType
import io.toasting.global.api.ApiResponse
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/v1/posts")
@Tag(name = "Post", description = "게시글 관련 API")
internal class PostController(
    private val postService: PostService,
    private val memberUuidConverter: MemberUuidConverter,
) {
    @GetMapping("/search")
    @Operation(summary = "게시글 검색, 로그인하지 않은 경우 isBookmarked는 false를 반환한다.")
    fun searchPosts(
        @AuthenticationPrincipal memberDetails: MemberDetails?,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = arrayOf("postedAt"),
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
        @RequestParam("keyword", required = false)
        @Size(
            max = 255,
            message = "검색어는 최대 255자입니다."
        ) keyword: String?,
    ): ApiResponse<PageResponse<SearchPostsResponse>> {
        val memberId = memberDetails?.let { memberUuidConverter.toMemberId(it.username) }
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
        @PathVariable("sourceType") sourceType: SourceType,
        @PathVariable("id")
        @Size(
            min = 2,
            max = 255,
            message = "아이디는 2 ~ 255글자 입니다."
        ) id: String,
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
