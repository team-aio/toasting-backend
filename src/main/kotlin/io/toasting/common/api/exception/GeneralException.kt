package team.toasting.api.exception

import team.toasting.api.code.BaseErrorCode
import team.toasting.api.code.ErrorReasonDTO

open class GeneralException(
    private val errorCode: BaseErrorCode
) : RuntimeException() {
    fun getErrorReason(): ErrorReasonDTO = errorCode.getReason()
}