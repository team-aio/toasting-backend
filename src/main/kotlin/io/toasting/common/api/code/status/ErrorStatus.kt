package team.toasting.api.code.status

import org.springframework.http.HttpStatus
import team.toasting.api.code.BaseErrorCode
import team.toasting.api.code.ErrorReasonDTO

enum class ErrorStatus(
    val httpStatus: HttpStatus,
    val status: String,
    private val message: String
) : BaseErrorCode {
    // 가장 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4010", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4030", "금지된 요청입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "COMMON4031", "데이터베이스 유효성 에러")
    ;

    override fun getReason() =
        ErrorReasonDTO(
            httpStatus = this.httpStatus,
            code = this.status,
            message = this.message
        )
}