package io.toasting.domain.member.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class FollowExceptionHandler {
    class SelfFollowException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}
