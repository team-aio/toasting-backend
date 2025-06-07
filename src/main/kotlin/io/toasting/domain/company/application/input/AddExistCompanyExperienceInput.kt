package io.toasting.domain.company.application.input

import io.toasting.domain.company.entity.CompanyExperience
import java.time.LocalDate

data class AddExistCompanyExperienceInput(
    val memberId: Long,
    val companyId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val position: String,
    val activities: String,
) {
    fun toEntity(): CompanyExperience = CompanyExperience(
        startDate = startDate,
        endDate = endDate,
        position = position,
        activities = activities,
        companyId = companyId,
        memberId = memberId,
        isView = true,
    )
}