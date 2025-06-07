package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CompanyExceptionHandler {
    class CompanyNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}