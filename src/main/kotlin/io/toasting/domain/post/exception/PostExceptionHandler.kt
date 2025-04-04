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
}