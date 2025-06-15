package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CompanyExperienceExceptionHandler(errorCode: BaseErrorCode) : GeneralException(errorCode) {
    class CompanyExperienceNotFoundException(
        errorCode: BaseErrorCode,
    ) : CompanyExperienceExceptionHandler(errorCode)

    class UnauthorizedDeleteCompanyExperienceException(
        errorCode: BaseErrorCode,
    ) : CompanyExperienceExceptionHandler(errorCode)

    class UnauthorizedUpdateException(
        errorCode: BaseErrorCode,
    ) : CompanyExperienceExceptionHandler(errorCode)

    class UnauthorizedUpdateIsViewException(
        errorCode: BaseErrorCode,
    ) : CompanyExperienceExceptionHandler(errorCode)
}