package io.toasting.domain.crumb.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.crumb.application.output.FindCrumbOutput
import io.toasting.domain.crumb.application.provided.CrumbFinder
import io.toasting.domain.crumb.exception.CrumbExceptionHandler.CrumbNotFoundException
import io.toasting.domain.crumb.repository.CrumbRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FindCrumbService(
    private val crumbRepository: CrumbRepository,
) : CrumbFinder {
    override fun findBy(
        memberId: Long,
        activityDate: LocalDate
    ): FindCrumbOutput =
        crumbRepository
            .findByMemberIdAndActivityDate(memberId, activityDate)
            ?.let { crumb -> FindCrumbOutput.from(crumb) }
            ?: run { throw CrumbNotFoundException(ErrorStatus.CRUMB_NOT_FOUND) }

    override fun findAllBy(
        memberId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<FindCrumbOutput> =
        crumbRepository
            .findByMemberIdAndActivityDateBetween(memberId, startDate, endDate)
            .map { crumb -> FindCrumbOutput.from(crumb) }
}