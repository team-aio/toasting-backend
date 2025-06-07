package io.toasting.domain.company.application.output

import io.toasting.domain.company.entity.Company
import io.toasting.domain.company.entity.CompanyExperience
import io.toasting.domain.company.entity.CustomCompanyExperience

data class GetCompanyExperienceOutput(
    val experienceId: Long,
    val name: String,
    val position: String,
    val startDate: String,
    val endDate: String?,
    val imageUrl: String,
    val activities: String,
    val isCustom: Boolean,
    val isView: Boolean,
) {
    companion object {
        fun of(companyExperience: CompanyExperience, company: Company): GetCompanyExperienceOutput {
            return GetCompanyExperienceOutput(
                experienceId = companyExperience.id!!,
                name = company.name,
                position = companyExperience.position,
                startDate = companyExperience.startDate.toString(),
                endDate = companyExperience.endDate?.toString(),
                imageUrl = company.profileImage,
                activities = companyExperience.activities,
                isCustom = false,
                isView = companyExperience.isView
            )
        }

        fun of(customCompanyExperience: CustomCompanyExperience): GetCompanyExperienceOutput {
            return GetCompanyExperienceOutput(
                experienceId = customCompanyExperience.id!!,
                name = customCompanyExperience.companyName,
                position = customCompanyExperience.position,
                startDate = customCompanyExperience.startDate.toString(),
                endDate = customCompanyExperience.endDate?.toString(),
                imageUrl = customCompanyExperience.profileImage,
                activities = customCompanyExperience.activities,
                isCustom = true,
                isView = customCompanyExperience.isView
            )
        }
    }
}
