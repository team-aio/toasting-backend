package io.toasting.global.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @receiver : String
 * @return : LocalDate? (실패시 Null)
 *
 * yyyy-MM-dd 형식의 문자열을 LocalDate로 변환
 *
 */
fun String.toLocalDate(): LocalDate? {
    return runCatching {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    }.getOrNull()
}