package io.toasting.domain.member.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class MemberExceptionHandler {
    class MemberNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class MemberNameDuplicationException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class SocialMemberAlreadySignUpException(
        errorCode: BaseErrorCode,
    ) : GeneralException(
        errorCode,
    )

    class MemberException(
        errorCode: BaseErrorCode,
    ) : GeneralException(
        errorCode,
    )

}
