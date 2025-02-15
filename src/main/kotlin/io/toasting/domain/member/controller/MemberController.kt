package io.toasting.domain.member.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.domain.member.controller.request.LoginGoogleRequest
import io.toasting.domain.member.controller.response.LoginGoogleResponse
import io.toasting.global.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member")
@Tag(name = "Member", description = "회원 관련 API")
class MemberController {
    private val log = KotlinLogging.logger {}

    @PostMapping("/login/google")
    @Operation(summary = "구글 로그인", description = "구글 소셜 로그인을 통해 로그인을 시도합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "로그인 성공, 기존에 가입했던 유저",
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201",
        description = "로그인 성공, 신규 유저",
    )
    fun loginGoogle(
        @Valid @RequestBody loginGoogleRequest: LoginGoogleRequest,
    ): ApiResponse<LoginGoogleResponse> {
        log.info { "loginGoogleRequest: $loginGoogleRequest" }
        return ApiResponse.onSuccess(
            data =
                LoginGoogleResponse(
                    accessToken = loginGoogleRequest.email,
                    refreshToken = loginGoogleRequest.snsId,
                ),
        )
    }
}
