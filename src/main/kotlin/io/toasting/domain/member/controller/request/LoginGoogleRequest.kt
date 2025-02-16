package io.toasting.domain.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import io.toasting.domain.member.application.input.LoginGoogleInput
import io.toasting.domain.member.vo.SocialType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(title = "구글 로그인 요청 DTO", description = "구글 로그인 요청")
data class LoginGoogleRequest(
    @Schema(description = "이메일", example = "howudong@example.com")
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String,
    @Schema(description = "유저 이름", example = "howudong")
    @field:NotBlank(message = "유저 이름은 빈 값이 될 수 없습니다.")
    val username: String,
    @Schema(description = "snsType", example = "google")
    @NotNull(message = "snsType은 필수입니다.")
    val snsType: String,
    @Schema(description = "snsId", example = "1234567890")
    @NotNull(message = "snsId는 필수입니다.")
    val snsId: String,
) {
    fun toInput() =
        LoginGoogleInput(
            email = email,
            username = username,
            socialType = SocialType.from(snsType),
            externalId = snsId,
        )
}
