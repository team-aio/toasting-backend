package io.toasting.global.extension

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler.InvalidDateFormatException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @receiver : String
 * @throws: InvalidDateFormatException
 * @return : LocalDate
 *
 * yyyy-MM-dd 형식의 문자열을 LocalDate로 변환
 *
 */
fun String.toLocalDateOrThrow(): LocalDate {
    return runCatching {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    }.getOrElse {
        throw InvalidDateFormatException(ErrorStatus.INVALID_DATE_FORMAT)
    }
}