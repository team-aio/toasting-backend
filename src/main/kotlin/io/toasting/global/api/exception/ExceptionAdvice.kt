package io.toasting.global.api.exception

import io.toasting.api.code.status.ErrorStatus
import io.toasting.global.api.ErrorApiResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionAdvice : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val errorMessage =
            ex.bindingResult
                .fieldErrors
                .first()
                .defaultMessage
                ?: throw RuntimeException("MethodArgumentNotValidException 추출 오류 발생")

        return handleExceptionInternalConstraint(ex, errorMessage, HttpHeaders.EMPTY, request)
    }

    @ExceptionHandler
    fun validatedException(
        e: ConstraintViolationException,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val errorMessage =
            e.constraintViolations
                .stream()
                .map { it.message }
                .findFirst()
                .orElseThrow { RuntimeException("ConstrainViolation 추출 오류 발생") }

        return handleExceptionInternalConstraint(e, errorMessage, HttpHeaders.EMPTY, request)
    }

    @ExceptionHandler
    fun handleCustomException(
        ex: GeneralException,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val errorReasonDto = ex.getErrorReason()
        val body = ErrorApiResponse.onFailure(errorReasonDto.code, errorReasonDto.message, null)
        return toResponseEntity(
            ex,
            HttpHeaders.EMPTY,
            request,
            errorReasonDto.httpStatus,
            body,
        )
    }

    @ExceptionHandler
    fun globalException(
        ex: Exception,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        ex.printStackTrace()
        val errorReason = ErrorStatus.INTERNAL_SERVER_ERROR.getReason()
        val errorPoint = ErrorApiResponse.onFailure(errorReason.code, errorReason.message, ex.message)

        return toResponseEntity(ex, HttpHeaders.EMPTY, request, HttpStatus.INTERNAL_SERVER_ERROR, errorPoint)
    }

    private fun handleExceptionInternalConstraint(
        ex: Exception,
        errorMessage: String,
        headers: HttpHeaders,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val body: ErrorApiResponse<Any> =
            ErrorApiResponse.onFailure(
                ErrorStatus.VALIDATION_FAIL.status,
                errorMessage,
                null,
            )

        return toResponseEntity(ex, headers, request, ErrorStatus.VALIDATION_FAIL.httpStatus, body)
    }

    private fun toResponseEntity(
        e: Exception,
        headers: HttpHeaders,
        request: WebRequest,
        httpStatus: HttpStatus,
        body: ErrorApiResponse<*>,
    ): ResponseEntity<Any>? =
        super.handleExceptionInternal(
            e,
            body,
            headers,
            httpStatus,
            request,
        )
}
