package io.toasting.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.ReIssueService
import io.toasting.domain.member.controller.response.ReIssueResponse
import io.toasting.global.api.ApiResponse
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.constants.Auth
import io.toasting.global.extension.CookieExtension
import io.toasting.global.extension.createCookie
import io.toasting.global.extension.findRefreshTokenOrNull
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
@Tag(name = "Member", description = "회원 관련 API")
class ReIssueController(
    private val reIssueService: ReIssueService,
    @Value("\${spring.jwt.refresh-token-expired-ms}") private val refreshTokenExpiredMs: Long,
) {
    @PostMapping("/reissue")
    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 통해 액세스 토큰을 재발급합니다.")
    fun reissue(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ApiResponse<Unit> {
        val refreshToken = getRefreshTokenOrThrow(request)
        val reIssueResponse = ReIssueResponse.from(reIssueService.reIssueAccessToken(refreshToken))

        response.setHeader(Auth.ACCESS_TOKEN, reIssueResponse.newAccessToken)
        response.addCookie(
            CookieExtension.createCookie(
                Auth.REFRESH_TOKEN,
                reIssueResponse.newRefreshToken,
                (refreshTokenExpiredMs / 1000).toInt(),
            ),
        )
        return ApiResponse.onSuccess()
    }

    private fun getRefreshTokenOrThrow(request: HttpServletRequest) =
        request.cookies.findRefreshTokenOrNull()
            ?: throw AuthExceptionHandler.TokenNotFoundException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND)
}
