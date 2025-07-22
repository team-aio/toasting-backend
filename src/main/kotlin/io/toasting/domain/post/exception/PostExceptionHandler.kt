package io.toasting.domain.post.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

sealed class PostExceptionHandler {
    class PostNotFoundException(
        errorCode: BaseErrorCode
    ) : GeneralException(errorCode)

    class AlreadyLinkedBlog(
        errorCode: BaseErrorCode
    ) : GeneralException(errorCode)

    class AlreadyPinnedPostException(
        errorCode: BaseErrorCode
    ) : GeneralException(errorCode)

    class NotPinnedPostException(
        errorCode: BaseErrorCode
    ) : GeneralException(errorCode)

    class NotWriterByPinException(
        errorCode: BaseErrorCode
    ) : GeneralException(errorCode)

    class NotLinkedBlog(
        errorCode: BaseErrorCode
    ) : GeneralException(errorCode)
}