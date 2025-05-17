package io.toasting.domain.company.controller.request

data class AddCompanyExperienceRequest(
    val isCustom: Boolean,
    val companyId: Boolean,
    val name: String,
    val startDate: String,
    val endDate: String,
    val position: String,
    val activities: String,
    val imageUrl: String,
)
