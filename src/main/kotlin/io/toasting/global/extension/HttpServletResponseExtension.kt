package io.toasting.global.extension

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.toasting.api.code.status.ErrorStatus
import jakarta.servlet.http.HttpServletResponse

fun HttpServletResponse.sendErrorResponse(errorStatus: ErrorStatus) {
    this.status = errorStatus.getReason().httpStatus.value()
    this.contentType = "application/json"
    this.characterEncoding = "UTF-8"

    jacksonObjectMapper()
        .writeValueAsString(errorStatus.getReason())
        .let { error -> this.writer.write(error) }
}
