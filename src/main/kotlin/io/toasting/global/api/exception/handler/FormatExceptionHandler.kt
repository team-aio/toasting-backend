package io.toasting.global.api.exception.handler

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class FormatExceptionHandler(errorCode: BaseErrorCode) : GeneralException(errorCode) {
    class InvalidDateFormatException(
        errorCode: BaseErrorCode,
    ) : FormatExceptionHandler(errorCode)
}