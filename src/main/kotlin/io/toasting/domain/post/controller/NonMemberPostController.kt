package io.toasting.domain.post.controller

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
class NonMemberPostController(
    private val nonMemberPostService: NonMemberPostService
) {

    @GetMapping("/search")
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