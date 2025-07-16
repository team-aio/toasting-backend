package io.toasting.domain.crumb.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CrumbExceptionHandler(errorCode: BaseErrorCode) : GeneralException(errorCode) {
    class CrumbNotFoundException(
        errorCode: BaseErrorCode
    ) : CrumbExceptionHandler(errorCode)
}