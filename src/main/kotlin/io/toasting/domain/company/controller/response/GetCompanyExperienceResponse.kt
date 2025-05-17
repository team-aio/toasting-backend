package io.toasting.domain.company.controller.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GetCompanyExperienceResponse(
    val id: Long,
    val name: String,
    val position: String,
    val startDate: String,
    val endDate: String?,
    val imageUrl: String,
    val activities: String,
    val isCustom: Boolean,
    val isView: Boolean,
)