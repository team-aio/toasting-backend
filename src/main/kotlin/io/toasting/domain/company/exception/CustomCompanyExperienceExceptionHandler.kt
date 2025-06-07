package io.toasting.domain.company.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class CustomCompanyExperienceExceptionHandler {
    class CustomCompanyExperienceNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class UnauthorizedDeleteCustomCompanyExperienceException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class UnauthorizedUpdateException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class UnauthorizedUpdateIsViewException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}