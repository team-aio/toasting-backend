package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CompanyExceptionHandler(errorCode: BaseErrorCode) : GeneralException(errorCode) {
    class CompanyNotFoundException(
        errorCode: BaseErrorCode,
    ) : CompanyExceptionHandler(errorCode)
}