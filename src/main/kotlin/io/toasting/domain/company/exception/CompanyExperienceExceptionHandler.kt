package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CompanyExperienceExceptionHandler {
    class InvalidDateFormatException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}