package io.toasting.domain.post.controller

import io.toasting.api.PageResponse
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
internal class PostController(
    private val postService: PostService
) {
    @GetMapping("/search")
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
        val output = postService.searchPost(memberDetails, keyword, pageable)
        val response = output.content.map { SearchPostsResponse.from(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(response,
                output.totalElements,
                output.totalPages
            )
        )
    }

    @PostMapping("/blog/{sourceType}/{id}")
    fun linkBlog(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("sourceType") sourceType: String,
        @PathVariable("id") id: String,
    ) : ApiResponse<Unit> {
        postService.linkBlog(memberDetails, id, sourceType)
        return ApiResponse.onSuccess()
    }

    @GetMapping("/{postId}")
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
