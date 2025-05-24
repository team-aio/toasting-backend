package io.toasting.global.security.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.global.api.exception.handler.AuthExceptionHandler
import io.toasting.global.constants.Auth
import io.toasting.global.extension.sendErrorResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

private const val EMPTY = ""

class JwtFilter(
    private val jwtFactory: JwtFactory,
) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}

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
            .onFailure { exception ->
                val errorStatus = findTokenException(exception)
                response.sendErrorResponse(errorStatus)
            }
    }

    private fun sendAuthToken(
        accessToken: String,
        filterChain: FilterChain,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val memberUuid =
            jwtFactory.memberUuid(accessToken)
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

        val memberDetails = MemberDetails(role, memberUuid)
        val authToken = UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.authorities)
        SecurityContextHolder.getContext().authentication = authToken

        filterChain.doFilter(request, response)
    }

    private fun findTokenException(exception: Throwable) =
        when (exception) {
            is AuthExceptionHandler.TokenExpiredException -> ErrorStatus.ACCESS_TOKEN_EXPIRED

            is AuthExceptionHandler.TokenNotFoundException -> ErrorStatus.UNAUTHORIZED

            else -> ErrorStatus.TOKEN_ERROR
        }
}
