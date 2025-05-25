package io.toasting.domain.company.application.input

import io.toasting.domain.company.entity.CustomCompanyExperience

data class UpdateCustomCompanyExperienceIsViewInput(
    val memberId : Long,
    val experienceId: Long,
    val isView: Boolean
) {
    fun toEntity(customCompanyExperience: CustomCompanyExperience) = CustomCompanyExperience(
        customCompanyExperience.id!!,
        customCompanyExperience.startDate,
        customCompanyExperience.endDate,
        customCompanyExperience.position,
        customCompanyExperience.activities,
        customCompanyExperience.profileImage,
        customCompanyExperience.companyName,
        customCompanyExperience.memberId,
        isView,
    )
}
