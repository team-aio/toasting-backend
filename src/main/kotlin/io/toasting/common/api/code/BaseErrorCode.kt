package team.toasting.api.code

interface BaseErrorCode {
    fun getReason(): ErrorReasonDTO
}