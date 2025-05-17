package io.toasting.domain.company.application.input

data class AddExistCompanyExperienceInput(
    val memberId: Long,
    val companyId: Boolean,
    val name: String,
    val startDate: String,
    val endDate: String,
    val position: String,
    val activities: String,
    val imageUrl: String,
)