package io.toasting.domain.member.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.code.status.SuccessStatus
import io.toasting.domain.member.application.LoginMemberService
import io.toasting.domain.member.application.SignUpMemberService
import io.toasting.domain.member.controller.request.LoginGoogleRequest
import io.toasting.domain.member.controller.request.SignUpSocialLoginRequest
import io.toasting.domain.member.vo.SocialType
import io.toasting.global.api.ApiResponse
import io.toasting.global.constants.Auth
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member")
@Tag(name = "Member", description = "회원 관련 API")
class MemberController(
    @Value("\${spring.jwt.refresh-token-expired-ms}") private val refreshTokenExpiredMs: Long,
    private val loginMemberService: LoginMemberService,
    private val signUpMemberService: SignUpMemberService,
) {
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
        response: HttpServletResponse,
    ): ApiResponse<Unit> {
        val loginGoogleOutput = processGoogleLogin(loginGoogleRequest)

        if (loginGoogleOutput != null) {
            response.setHeader(Auth.ACCESS_TOKEN, loginGoogleOutput.accessToken)
            response.addCookie(createCookie(Auth.REFRESH_TOKEN, loginGoogleOutput.refreshToken))
            return ApiResponse.onSuccess()
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CREATED.status, null)
    }

    @PostMapping("/signup")
    @Operation(summary = "소셜 로그인을 통한 회원가입", description = "소셜 로그인을 통한 회원가입을 시도합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "회원가입 성공",
    )
    fun signUpBySocialLogin(
        @RequestParam("snsType") snsType: String,
        @Valid @RequestBody signUpSocialLoginRequest: SignUpSocialLoginRequest,
    ): ApiResponse<Unit> {
        validate(signUpSocialLoginRequest)
        signUpMemberService.signUpBySocialLogin(signUpSocialLoginRequest.toInput())
        return ApiResponse.onSuccess()
    }

    private fun createCookie(
        key: String,
        value: String,
    ): Cookie =
        Cookie(key, value).apply {
            isHttpOnly = true
            maxAge = (refreshTokenExpiredMs / 1000).toInt()
            path = "/"
        }

    private fun processGoogleLogin(loginGoogleRequest: LoginGoogleRequest) =
        loginGoogleRequest
            .toInput()
            .let { loginGoogleInput -> loginMemberService.loginGoogle(loginGoogleInput) }

    private fun validate(signUpSocialLoginRequest: SignUpSocialLoginRequest) {
        SocialType.from(signUpSocialLoginRequest.snsType)
    }
}
