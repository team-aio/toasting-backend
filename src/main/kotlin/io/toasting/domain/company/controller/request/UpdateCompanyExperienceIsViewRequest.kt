package io.toasting.domain.company.controller.request

import io.toasting.domain.company.application.input.UpdateCustomCompanyExperienceIsViewInput
import io.toasting.domain.company.application.input.UpdateExistCompanyExperienceIsViewInput

data class UpdateCompanyExperienceIsViewRequest(
    val experienceId: Long,
    val isView: Boolean,
    val isCustom: Boolean
) {
    fun toCustomInput(memberId: Long) = UpdateCustomCompanyExperienceIsViewInput(
        memberId = memberId,
        experienceId = experienceId,
        isView = isView,
        isCustom = isCustom,
    )

    fun toExistInput(memberId: Long) = UpdateExistCompanyExperienceIsViewInput(
        memberId = memberId,
        experienceId = experienceId,
        isView = isView,
        isCustom = isCustom,
    )
}