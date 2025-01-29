package team.toasting.api.exception.handler

import team.toasting.api.code.BaseErrorCode
import team.toasting.api.exception.GeneralException

class ExampleHandler(errorCode: BaseErrorCode) : GeneralException(errorCode) {

}