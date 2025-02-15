package io.toasting.domain.member.controller.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

@Schema(description = "구글 로그인 요청")
class LoginGoogleRequest(
    @Schema(description = "이메일", example = "howudong@example.com")
    @field:Email(message = "이메일 형식이 아닙니다.")
    val email: String,
    @Schema(description = "snsType", example = "google")
    @NotNull(message = "snsType은 필수입니다.")
    val snsType: String,
    @Schema(description = "snsId", example = "1234567890")
    @NotNull(message = "snsId는 필수입니다.")
    val snsId: String,
)
