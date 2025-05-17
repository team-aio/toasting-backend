package io.toasting.domain.company.controller.request

data class UpdateCompanyExperienceRequest(
    val experienceId: Long,
    val isCustom: Boolean,
    val companyId: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val position: String,
    val activities: String,
    val imageUrl: String,
)
