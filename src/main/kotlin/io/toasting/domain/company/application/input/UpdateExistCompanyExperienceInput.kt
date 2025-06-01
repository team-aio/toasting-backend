package io.toasting.domain.company.application.input

import io.toasting.domain.company.entity.Company
import io.toasting.domain.company.entity.CompanyExperience
import java.time.LocalDate

data class UpdateExistCompanyExperienceInput(
    val memberId: Long,
    val experienceId: Long,
    val companyId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val position: String,
    val activities: String,
) {
    fun toEntity(company: Company, companyExperience: CompanyExperience) = CompanyExperience(
        companyExperience.id!!,
        startDate,
        endDate,
        position,
        activities,
        company.id!!,
        memberId,
        companyExperience.isView,
    )
}