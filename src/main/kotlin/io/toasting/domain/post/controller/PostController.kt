package io.toasting.domain.post.controller

import io.toasting.api.PageResponse
import io.toasting.domain.post.controller.response.SearchPostsResponse
import io.toasting.global.api.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/posts")
internal class PostController {
    @GetMapping("/search")
    fun searchPosts(
        @PageableDefault(
            page = 0,
            size = 10,
            sort = arrayOf("postedAt"),
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
    ): ApiResponse<PageResponse<SearchPostsResponse>> {
        val content = List(10) { SearchPostsResponse.mock() }
        return ApiResponse.onSuccess(PageResponse.of(content, 10, 100))
    }
}
