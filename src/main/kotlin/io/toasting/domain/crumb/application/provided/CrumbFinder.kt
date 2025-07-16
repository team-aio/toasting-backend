package io.toasting.domain.crumb.application.provided

import io.toasting.domain.crumb.application.output.FindCrumbOutput
import java.time.LocalDate

/**
 * 빵가루 기록 조회에 관한 인터페이스
 */
interface CrumbFinder {
    fun findBy(memberId: Long, activityDate: LocalDate): FindCrumbOutput

    fun findAllBy(memberId: Long, startDate: LocalDate, endDate: LocalDate): List<FindCrumbOutput>
}