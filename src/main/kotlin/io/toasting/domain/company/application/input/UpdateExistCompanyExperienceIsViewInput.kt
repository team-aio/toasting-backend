package io.toasting.domain.company.application.input

import io.toasting.domain.company.entity.CompanyExperience

data class UpdateExistCompanyExperienceIsViewInput(
    val memberId: Long,
    val experienceId: Long,
    val isView: Boolean
) {
    fun toEntity(companyExperience: CompanyExperience) = CompanyExperience(
        companyExperience.id!!,
        companyExperience.startDate,
        companyExperience.endDate,
        companyExperience.position,
        companyExperience.activities,
        companyExperience.companyId,
        companyExperience.memberId,
        isView,
    )
}
