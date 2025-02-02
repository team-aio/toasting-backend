package io.toasting.api.code.status

import io.toasting.api.code.BaseCode
import io.toasting.api.code.ReasonDTO
import org.springframework.http.HttpStatus

enum class SuccessStatus(
    private val httpStatus: HttpStatus,
    val code: String,
    val message: String,
) : BaseCode {
    // 가장 일반적인 응답
    OK(HttpStatus.OK, "COMMON200", "요청 성공"),

    EXAMPLE_SAVE(HttpStatus.CREATED, "EXAMPLE_CREATED", "샘플 저장 성공"),
    EXAMPLE_OK(HttpStatus.OK, "EXAMPLE_OK", "샘플 가져오기 성공"),
    ;

    override fun getReason() =
        ReasonDTO(
            httpStatus = this.httpStatus,
            code = this.code,
            message = this.message,
        )
}
