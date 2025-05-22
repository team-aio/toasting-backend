package io.toasting.domain.company.application.input

import io.toasting.domain.company.entity.CustomCompanyExperience
import java.time.LocalDate

data class AddCustomCompanyExperienceInput(
    val memberId: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val position: String,
    val activities: String,
    val imageUrl: String,
) {
    fun toEntity() = CustomCompanyExperience(
        startDate = startDate,
        endDate = endDate,
        position = position,
        activities = activities,
        profileImage = imageUrl,
        companyName = name,
        memberId = memberId,
        isView = true,
    )
}