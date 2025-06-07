package io.toasting.domain.company.controller.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.toasting.domain.company.application.output.GetCompanyExperienceOutput

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetCompanyExperienceResponse(
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
        fun from(getCompanyExperienceOutput: GetCompanyExperienceOutput) = GetCompanyExperienceResponse(
            experienceId = getCompanyExperienceOutput.experienceId,
            name = getCompanyExperienceOutput.name,
            position = getCompanyExperienceOutput.position,
            startDate = getCompanyExperienceOutput.startDate,
            endDate = getCompanyExperienceOutput.endDate,
            imageUrl = getCompanyExperienceOutput.imageUrl,
            activities = getCompanyExperienceOutput.activities,
            isCustom = getCompanyExperienceOutput.isCustom,
            isView = getCompanyExperienceOutput.isView,
        )
    }
}