package io.toasting.global.api.exception.handler

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

class AuthExceptionHandler {
    class TokenExpiredException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class TokenNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}
