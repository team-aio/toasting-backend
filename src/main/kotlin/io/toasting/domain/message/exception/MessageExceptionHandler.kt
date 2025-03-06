package io.toasting.domain.message.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class MessageExceptionHandler {
    class ChatRoomNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class NotBelongsToChatRoomException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)

    class ChatMemberNotFoundException(
        errorCode: BaseErrorCode,
    ) : GeneralException(errorCode)
}