package team.toasting.api.code.status

import org.springframework.http.HttpStatus
import team.toasting.api.code.BaseCode
import team.toasting.api.code.ReasonDTO

enum class SuccessStatus(
    private val httpStatus: HttpStatus,
    val code: String,
    val message: String
) : BaseCode {
    // 가장 일반적인 응답
    OK(HttpStatus.OK, "COMMON200", "요청 성공");

    override fun getReason() =
        ReasonDTO(
            httpStatus = this.httpStatus,
            code = this.code,
            message = this.message
        )
}