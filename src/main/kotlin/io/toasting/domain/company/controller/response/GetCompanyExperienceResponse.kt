package io.toasting.domain.company.controller.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.toasting.domain.company.application.output.GetCompanyExperienceOutput

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetCompanyExperienceResponse(
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
        // TODO : 나중에 이 값 변경해야함
        fun from(getCompanyExperienceOutput: GetCompanyExperienceOutput) = GetCompanyExperienceResponse(
            name = getCompanyExperienceOutput.thisIsTestValue,
            position = getCompanyExperienceOutput.thisIsTestValue,
            startDate = getCompanyExperienceOutput.thisIsTestValue,
            endDate = getCompanyExperienceOutput.thisIsTestValue,
            imageUrl = getCompanyExperienceOutput.thisIsTestValue,
            activities = getCompanyExperienceOutput.thisIsTestValue,
            isCustom = false,
            isView = false,
        )
    }
}