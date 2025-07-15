package io.toasting.domain.crumb.repository

import io.toasting.domain.crumb.entity.Crumb
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface CrumbRepository : JpaRepository<Crumb, Long> {
    /**
     * @param start 시작날짜 (include)
     * @param end 종료날짜 (include)
     */
    fun findByMemberIdAndActivityDateBetween(memberId: Long, start: LocalDate, end: LocalDate)

    fun findByMemberIdAndActivityDate(memberId: Long, activityDate: LocalDate): Crumb?
}