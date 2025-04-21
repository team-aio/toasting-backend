package io.toasting.domain.post.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.post.application.out.GetPostDetailOutput
import io.toasting.domain.post.vo.SourceType
import java.time.LocalDateTime

class GetPostDetailResponse(
    @Schema(description = "게시글 id", example = "1")
    val id: Long,
    @Schema(description = "블로그 종류, tistory or velog", example = "tistory")
    val sourceType: SourceType,
    @Schema(description = "게시일", example = "2024-03-31T12:58:01")
    val postedAt: LocalDateTime? = null,
    @Schema(description = "게시글 내용, html 태그가 포함됨", example = "<p><a href=\\\"https://school.programm.,,,,,")
    val content: String,
    @Schema(description = "게시글 원래 주소", example = "https://howdong.tistory.com/20")
    val url: String,
    @Schema(description = "제목", example = "제목입니다.")
    val title: String,
    @Schema(description = "좋아요 수", example = "10")
    val likeCount: Int,
    @Schema(description = "작성자 id", example = "1")
    val memberId: Long,
    @Schema(description = "닉네임", example = "호우동")
    val nickname: String,
    @Schema(description = "프로필 사진")
    val profilePicture: String? = null
) {
    companion object {
        fun from(output: GetPostDetailOutput) = GetPostDetailResponse(
            id = output.id,
            sourceType = output.sourceType,
            postedAt = output.postedAt,
            content = output.content,
            url = output.url,
            title = output.title,
            likeCount = output.likeCount,
            memberId = output.memberId,
            nickname = output.nickname,
            profilePicture = output.profilePicture
        )
    }
}