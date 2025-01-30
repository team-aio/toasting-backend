package io.toasting.global.api.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.api.code.ErrorReasonDTO

open class GeneralException(
    private val errorCode: BaseErrorCode,
) : RuntimeException() {
    fun getErrorReason(): ErrorReasonDTO = errorCode.getReason()
}
