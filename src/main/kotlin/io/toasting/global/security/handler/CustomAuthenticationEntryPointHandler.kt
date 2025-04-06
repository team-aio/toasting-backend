package io.toasting.global.security.handler

import io.toasting.api.code.status.ErrorStatus
import io.toasting.global.extension.sendErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPointHandler : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.sendErrorResponse(
            errorStatus = ErrorStatus.FORBIDDEN,
        )
    }
}
