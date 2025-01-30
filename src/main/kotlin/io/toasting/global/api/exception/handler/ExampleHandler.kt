package io.toasting.global.api.exception.handler

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

class ExampleHandler(
    errorCode: BaseErrorCode,
) : GeneralException(errorCode)
