package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CompanyExperienceExceptionHandler {
    class CompanyExperienceNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class UnauthorizedDeleteCompanyExperienceException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}