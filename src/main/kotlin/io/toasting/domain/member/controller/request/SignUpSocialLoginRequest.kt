package io.toasting.domain.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.member.application.input.SignUpSocialLoginInput
import io.toasting.domain.member.vo.SocialType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(title = "소셜 로그인 회원가입 요청 DTO", description = "소셜 로그인 회원가입 요청")
data class SignUpSocialLoginRequest(
    @Schema(description = "이메일", example = "howudong@naver.com")
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String,
    @Schema(description = "유저 이름", example = "howudong")
    @field:NotBlank(message = "유저 이름은 빈 값이 될 수 없습니다.")
    val username: String,
    @Schema(description = "snsType", example = "google")
    val snsType: String,
    @Schema(description = "snsId", example = "1234567890")
    val snsId: String,
    @Schema(description = "닉네임", example = "호우동")
    @field:NotBlank(message = "닉네임은 빈 값이 될 수 없습니다.")
    val nickname: String,
    val tistoryId: String?,
    val velogId: String?,
) {
    fun toInput() =
        SignUpSocialLoginInput(
            email = email,
            username = username,
            nickname = nickname,
            socialType = SocialType.from(snsType),
            externalId = snsId,
            tistoryId = tistoryId,
            velogId = velogId,
        )
}
