package io.toasting.domain.post.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.post.application.out.SearchPostsOutput
import io.toasting.domain.post.vo.SourceType
import java.time.LocalDateTime

class SearchPostsResponse(
    @Schema(description = "게시글 id", example = "1")
    val id: Long,
    @Schema(description = "블로그 종류, tistory or velog", example = "tistory")
    val sourceType: SourceType,
    @Schema(description = "게시일", example = "2024-03-31T12:58:01")
    val postedAt: LocalDateTime? = null,
    @Schema(description = "미리보기 내용 100자, html 태그 없음", example = "미리보기입니다. 미리보기입니다. 미리보기입니다. 미리보기입니다. ")
    val shortContent: String,
    @Schema(description = "제목", example = "제목입니다.")
    val title: String,
    @Schema(description = "좋아요 수", example = "10")
    val likeCount: Int,
    @Schema(description = "작성자 id", example = "1")
    val memberId: Long,
    @Schema(description = "닉네임", example = "호우동")
    val nickname: String,
    @Schema(description = "프로필 사진")
    val profilePicture: String? = null,
    @Schema(description = "북마크 여부 true or false, 비로그인인 경우 false로 고정", example = "false")
    val isBookmarked: Boolean,
) {
    companion object {
        fun from(output: SearchPostsOutput): SearchPostsResponse {
            return SearchPostsResponse(
                id = output.id,
                sourceType = output.sourceType,
                postedAt = output.postedAt,
                shortContent = output.shortContent,
                title = output.title,
                likeCount = output.likeCount,
                memberId = output.memberId,
                nickname = output.nickname,
                profilePicture = output.profilePicture,
                isBookmarked = output.isBookmarked
            )
        }
    }
}
