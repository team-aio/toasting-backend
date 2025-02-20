package io.toasting.api.code.status

import io.toasting.api.code.BaseCode
import io.toasting.api.code.ReasonDTO
import org.springframework.http.HttpStatus

enum class SuccessStatus(
    val httpStatus: HttpStatus,
    val status: String,
) : BaseCode {
    // 가장 일반적인 응답
    OK(HttpStatus.OK, "COMMON200"),
    MEMBER_CREATED(HttpStatus.CREATED, "MEMBER_CREATED"),
    ;

    override fun getReason() =
        ReasonDTO(
            httpStatus = this.httpStatus,
            code = this.status,
        )
}
