package io.toasting.domain.company.controller.request

import io.toasting.domain.company.application.input.UpdateCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.UpdateExistCompanyExperienceInput
import io.toasting.global.extension.toLocalDateOrThrow

data class UpdateCompanyExperienceRequest(
    val experienceId: Long,
    val isCustom: Boolean,
    val companyId: Long,
    val name: String,
    val startDate: String,
    val endDate: String?,
    val position: String,
    val activities: String,
    val imageUrl: String,
) {
    fun toExistInput(memberId: Long): UpdateExistCompanyExperienceInput {
        return UpdateExistCompanyExperienceInput(
            memberId = memberId,
            experienceId = experienceId,
            companyId = companyId,
            startDate = startDate.toLocalDateOrThrow(),
            endDate = endDate?.toLocalDateOrThrow(),
            position = position,
            activities = activities,
        )
    }

    fun toCustomInput(memberId: Long): UpdateCustomCompanyExperienceInput {
        return UpdateCustomCompanyExperienceInput(
            memberId = memberId,
            experienceId = experienceId,
            name = name,
            startDate = startDate.toLocalDateOrThrow(),
            endDate = endDate?.toLocalDateOrThrow(),
            position = position,
            activities = activities,
            imageUrl = imageUrl,
        )
    }
}
