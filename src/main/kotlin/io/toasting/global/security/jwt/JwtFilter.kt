package io.toasting.global.security.jwt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.constants.Auth
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

private const val EMPTY = ""

class JwtFilter(
    private val jwtFactory: JwtFactory,
) : OncePerRequestFilter() {
    companion object {
        private val EXIST_MEMBER_NICKNAME_API =
            RequestMatcher { request ->
                request.requestURI == "/v1/member/exist" &&
                    request.getParameter("nickname")?.isNotEmpty() ?: false
            }
        private val GET_PROFILE_API =
            RequestMatcher { request ->
                request.requestURI == "/v1/member/profile" &&
                    request.getParameter("memberId")?.isNotEmpty() ?: false
            }
        private val EXCLUDE_PATHS =
            listOf(
                "/",
                "/h2-console/**",
                "/favicon.ico",
                "/error",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/api-test/**",
                "/v1/member/login/google",
                "/v1/member/signup",
            )
    }

    private val log = KotlinLogging.logger {}

    override fun shouldNotFilter(request: HttpServletRequest): Boolean =
        OrRequestMatcher(
            EXCLUDE_PATHS.map { AntPathRequestMatcher(it) } +
                listOf(EXIST_MEMBER_NICKNAME_API, GET_PROFILE_API),
        ).matches(request)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = request.getHeader(Auth.ACCESS_TOKEN)

        if (accessToken == null) {
            filterChain.doFilter(request, response)
            return
        }
        val trimmedAccessToken = accessToken.replace(Auth.JWT_PREFIX, EMPTY)

        jwtFactory
            .validateAccessToken(trimmedAccessToken)
            .onSuccess { sendAuthToken(trimmedAccessToken, filterChain, request, response) }
            .onFailure { exception -> sendErrorResponse(exception, response) }
    }

    private fun sendAuthToken(
        accessToken: String,
        filterChain: FilterChain,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val memberId =
            jwtFactory.memberId(accessToken)
                ?: run {
                    log.error { "Member id is null" }
                    return
                }

        val role =
            jwtFactory.role(accessToken)
                ?: run {
                    log.error { "Role is null" }
                    return
                }

        val memberDetails = MemberDetails(role, memberId.toLong())
        val authToken = UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.authorities)
        SecurityContextHolder.getContext().authentication = authToken

        filterChain.doFilter(request, response)
    }

    private fun sendErrorResponse(
        exception: Throwable,
        response: HttpServletResponse,
    ) {
        val errorStatus = findTokenException(exception)
        response.status = errorStatus.getReason().httpStatus.value()
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        jacksonObjectMapper()
            .writeValueAsString(errorStatus.getReason())
            .let { error -> response.writer.write(error) }
    }

    private fun findTokenException(exception: Throwable) =
        when (exception) {
            is AuthExceptionHandler.TokenExpiredException -> ErrorStatus.ACCESS_TOKEN_EXPIRED

            is AuthExceptionHandler.TokenNotFoundException -> ErrorStatus.UNAUTHORIZED

            else -> ErrorStatus.TOKEN_ERROR
        }
}
