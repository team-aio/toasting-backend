package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CustomCompanyExperienceExceptionHandler(errorCode: BaseErrorCode) : GeneralException(errorCode) {
    class CustomCompanyExperienceNotFoundException(
        errorCode: BaseErrorCode,
    ) : CustomCompanyExperienceExceptionHandler(errorCode)

    class UnauthorizedDeleteCustomCompanyExperienceException(
        errorCode: BaseErrorCode,
    ) : CustomCompanyExperienceExceptionHandler(errorCode)

    class UnauthorizedUpdateException(
        errorCode: BaseErrorCode,
    ) : CustomCompanyExperienceExceptionHandler(errorCode)

    class UnauthorizedUpdateIsViewException(
        errorCode: BaseErrorCode,
    ) : CustomCompanyExperienceExceptionHandler(errorCode)
}