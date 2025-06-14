package io.toasting.api.code.status

import io.toasting.api.code.BaseErrorCode
import io.toasting.api.code.ErrorReasonDTO
import org.springframework.http.HttpStatus

enum class ErrorStatus(
    val httpStatus: HttpStatus,
    val status: String,
    private val message: String,
) : BaseErrorCode {
    // 가장 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON4010", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON4030", "금지된 요청입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "COMMON4031", "데이터베이스 유효성 에러"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "COMMON4011", "액세스 토큰 유효기간이 지났습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "COMMON4012", "액세스 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "COMMON4013", "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "COMMON4014", "리프레시 토큰 유효기간이 지났습니다."),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "COMMON4013", "토큰 에러"),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "member를 찾을 수 없습니다."),
    MEMBER_NAME_DUPLICATION(HttpStatus.BAD_REQUEST, "MEMBER_NAME_DUPLICATION", "닉네임이 중복되었습니다."),
    MEMBER_NOT_MINE(HttpStatus.BAD_REQUEST, "MEMBER_NOT_MINE", "본인이 아닙니다."),
    ALERADY_SIGN_UP_MEMBER(HttpStatus.BAD_REQUEST, "", "이미 가입한 멤버입니다."),
    EXAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "EXAMPLE_NOT_FOUND,", "example 엔티티를 찾을 수 없습니다."),

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ROOM_NOT_FOUND", "chatRoom을 찾을 수 없습니다."),
    NOT_BELONG_TO_CHAT_ROOM(HttpStatus.BAD_REQUEST, "NOT_BELONGS_TO_CHAT_ROOM", "참여 중인 채팅방이 아닙니다."),
    PARTNER_NOT_FOUND(HttpStatus.BAD_REQUEST, "PARTNER_NOT_FOUND", "상대방을 찾을 수 없습니다."),
    CHAT_ROOM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CHAT_ROOM_ALREADY_EXISTS", "이미 채팅방이 존재합니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),
    SELF_FOLLOW_BANNED(HttpStatus.BAD_REQUEST, "SELF_FOLLOW_BANNED", "자신을 팔로우 할 수 없습니다."),
    ALREADY_LINKED_BLOG(HttpStatus.BAD_REQUEST, "ALREADY_LINKED_BLOG", "이미 블로그를 연동했습니다."),

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY_NOT_FOUND", "회사를 찾을 수 없습니다."),
    COMPANY_EXPERIENCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY_EXPERIENCE_NOT_FOUND", "경력을 찾을 수 없습니다."),
    CUSTOM_COMPANY_EXPERIENCE_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "CUSTOM_COMPANY_EXPERIENCE_NOT_FOUND",
        "직접 입력한 경력을 찾을 수 없습니다."
    ),
    UNAUTHORIZED_DELETE_COMPANY_EXPERIENCE(
        HttpStatus.BAD_REQUEST,
        "UNAUTHORIZED_DELETE_COMPANY_EXPERIENCE",
        "경력을 삭제할 권한이 없습니다."
    ),
    UNAUTHORIZED_DELETE_CUSTOM_COMPANY_EXPERIENCE(
        HttpStatus.BAD_REQUEST,
        "UNAUTHORIZED_DELETE_CUSTOM_COMPANY_EXPERIENCE",
        "직접 입력한 경력을 삭제할 권한이 없습니다."
    ),
    UNAUTHORIZED_UPDATE_COMPANY_EXPERIENCE(
        HttpStatus.BAD_REQUEST,
        "UNAUTHORIZED_UPDATE_COMPANY_EXPERIENCE",
        "경력을 수정할 권한이 없습니다."
    ),
    UNAUTHORIZED_UPDATE_CUSTOM_COMPANY_EXPERIENCE(
        HttpStatus.BAD_REQUEST,
        "UNAUTHORIZED_UPDATE_CUSTOM_COMPANY_EXPERIENCE",
        "직접 입력한 경력을 삭제할 권한이 없습니다."
    ),
    UNAUTHORIZED_UPDATE_COMPANY_EXPERIENCE_IS_VIEW(
        HttpStatus.BAD_REQUEST,
        "UNAUTHORIZED_UPDATE_COMPANY_EXPERIENCE_IS_VIEW",
        "숨기기 여부를 수정할 권한이 없습니다."
    ),
    UNAUTHORIZED_UPDATE_CUSTOM_COMPANY_EXPERIENCE_IS_VIEW(
        HttpStatus.BAD_REQUEST,
        "UNAUTHORIZED_UPDATE_CUSTOM_COMPANY_EXPERIENCE_IS_VIEW",
        "숨기기 여부를 수정할 권한이 없습니다."
    ),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "날짜 형식이 올바르지 않습니다.(yyyy-MM-dd 형식이어야 합니다.)"),
    ;

    override fun getReason() =
        ErrorReasonDTO(
            httpStatus = this.httpStatus,
            code = this.status,
            message = this.message,
        )
}
