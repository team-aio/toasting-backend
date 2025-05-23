package io.toasting.domain.company.controller.request

import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput

data class AddCompanyExperienceRequest(
    val isCustom: Boolean,
    val companyId: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val position: String,
    val activities: String,
    val imageUrl: String,
) {
    fun toExistInput(memberId: Long) = AddExistCompanyExperienceInput(
        memberId = memberId,
        companyId = companyId,
        name = name,
        startDate = startDate,
        endDate = endDate,
        position = position,
        activities = activities,
        imageUrl = imageUrl,
    )

    fun toCustomInput(memberId: Long) = AddCustomCompanyExperienceInput(
        memberId = memberId,
        companyId = companyId,
        name = name,
        startDate = startDate,
        endDate = endDate,
        position = position,
        activities = activities,
        imageUrl = imageUrl,
    )
}
