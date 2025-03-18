package io.toasting.domain.member.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.code.status.ErrorStatus
import io.toasting.api.code.status.SuccessStatus
import io.toasting.domain.member.application.CheckMemberService
import io.toasting.domain.member.application.GetProfileService
import io.toasting.domain.member.application.LoginMemberService
import io.toasting.domain.member.application.SignUpMemberService
import io.toasting.domain.member.controller.request.LoginGoogleRequest
import io.toasting.domain.member.controller.request.SignUpSocialLoginRequest
import io.toasting.domain.member.controller.response.GetMyProfileResponse
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.RefreshTokenRepository
import io.toasting.domain.member.vo.SocialType
import io.toasting.global.api.ApiResponse
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.constants.Auth
import io.toasting.global.extension.CookieExtension
import io.toasting.global.extension.createCookie
import io.toasting.global.extension.findRefreshTokenOrNull
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
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
    private val checkMemberService: CheckMemberService,
    private val getProfileService: GetProfileService,
    private val refreshTokenRepository: RefreshTokenRepository, // TODO : 의존성 방향만 맞춤, 바로 Repository를 호출하면 아면 추후 리팩토링
) {
    private val log = KotlinLogging.logger {}

    @PostMapping("/login/google")
    @Operation(summary = "구글 로그인", description = "구글 소셜 로그인을 통해 로그인을 시도합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "COMMON200",
        description = "로그인 성공, 기존에 가입했던 유저",
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "MEMBER_CREATE",
        description = "로그인 성공, 신규 유저",
    )
    fun loginGoogle(
        @Valid @RequestBody loginGoogleRequest: LoginGoogleRequest,
        response: HttpServletResponse,
    ): ApiResponse<Unit> {
        val loginGoogleOutput = processGoogleLogin(loginGoogleRequest)

        if (loginGoogleOutput != null) {
            response.setHeader(Auth.ACCESS_TOKEN, loginGoogleOutput.accessToken)
            response.addCookie(
                CookieExtension.createCookie(
                    Auth.REFRESH_TOKEN,
                    loginGoogleOutput.refreshToken,
                    (refreshTokenExpiredMs / 1000).toInt(),
                ),
            )
            return ApiResponse.onSuccess()
        }
        return ApiResponse.onSuccess(SuccessStatus.MEMBER_CREATED.status, null)
    }

    @GetMapping("/exist")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복을 확인합니다.")
    fun isExistNickname(
        @RequestParam("nickname", required = true) nickname: String,
    ): ApiResponse<Unit> {
        checkMemberService.checkMemberNicknameIsDuplicated(nickname)
        return ApiResponse.onSuccess()
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
        validate(snsType, signUpSocialLoginRequest)
        signUpMemberService.signUpBySocialLogin(signUpSocialLoginRequest.toInput())
        return ApiResponse.onSuccess()
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 ", description = "로그아웃을 하여 리프레시 토큰을 삭제 및 만료시킵니디.")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val refreshToken = request.cookies.findRefreshTokenOrNull()
        val expiredRefreshToken = refreshToken?.let { CookieExtension.createCookie(Auth.REFRESH_TOKEN, it, 0) }

        if (refreshToken == null) {
            log.info { "RefreshToken is not found" }
            throw AuthExceptionHandler.TokenNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND)
        }
        response.addCookie(expiredRefreshToken)
        refreshTokenRepository.deleteByToken(refreshToken)
    }

    @GetMapping("/my-profile")
    @Operation(summary = "내 프로필 조회", description = "액세스 토큰을 통해 내 프로필을 조회합니다.")
    fun getMyProfile(
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<GetMyProfileResponse> {
        val memberId = memberDetails.username.toLong()

        return getProfileService
            .getProfile(memberId)
            .let { GetMyProfileResponse.from(it) }
            .let { response -> ApiResponse.onSuccess(response) }
    }

    private fun processGoogleLogin(loginGoogleRequest: LoginGoogleRequest) =
        loginGoogleRequest
            .toInput()
            .let { loginGoogleInput -> loginMemberService.loginGoogle(loginGoogleInput) }

    private fun validate(
        snsType: String,
        signUpSocialLoginRequest: SignUpSocialLoginRequest,
    ) {
        SocialType.from(snsType)
        SocialType.from(signUpSocialLoginRequest.snsType)
    }
}
