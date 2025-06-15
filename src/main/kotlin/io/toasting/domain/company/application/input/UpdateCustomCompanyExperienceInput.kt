package io.toasting.domain.company.application.input

import io.toasting.domain.company.entity.CustomCompanyExperience
import java.time.LocalDate

data class UpdateCustomCompanyExperienceInput(
    val memberId: Long,
    val experienceId: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val position: String,
    val activities: String,
    val imageUrl: String,
) {
    fun toEntity(customCompanyExperience: CustomCompanyExperience) = CustomCompanyExperience(
        customCompanyExperience.id!!,
        startDate,
        endDate,
        position,
        activities,
        imageUrl,
        name,
        memberId,
        customCompanyExperience.isView,
    )
}