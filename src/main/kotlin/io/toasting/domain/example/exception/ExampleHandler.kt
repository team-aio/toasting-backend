package io.toasting.domain.example.exception

import io.toasting.api.code.BaseErrorCode
import io.toasting.global.api.exception.GeneralException

class ExampleHandler(
    errorCode: BaseErrorCode,
) : GeneralException(errorCode)
