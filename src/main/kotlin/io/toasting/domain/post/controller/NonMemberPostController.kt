package io.toasting.domain.post.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.PageResponse
import io.toasting.domain.post.application.NonMemberPostService
import io.toasting.domain.post.controller.response.SearchPostsResponse
import io.toasting.global.api.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/non-member/posts")
@Tag(name = "NonMember-Post", description = "비로그인 게시글 관련 API")
class NonMemberPostController(
    private val nonMemberPostService: NonMemberPostService
) {

    @GetMapping("/search")
    @Operation(summary = "로그인 하지 않은 게시글 검색", description = "북마크 여부는 모두 false로 응답합니다.")
    fun searchPostWithoutMember(
        @PageableDefault(
            page = 0,
            size = 10,
            sort = arrayOf("postedAt"),
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
        @RequestParam("keyword", required = false) keyword: String?,
    ): ApiResponse<PageResponse<SearchPostsResponse>> {
        val output = nonMemberPostService.searchPostWithoutMember(keyword, pageable)
        val response = output.content.map { SearchPostsResponse.from(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(
                response,
                output.totalElements,
                output.totalPages
            )
        )
    }
}