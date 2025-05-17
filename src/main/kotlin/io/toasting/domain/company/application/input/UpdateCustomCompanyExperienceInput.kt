package io.toasting.domain.company.application.input

data class UpdateCustomCompanyExperienceInput(
    val memberId: Long,
    val experienceId: Long,
    val companyId: Long,
    val name: String,
    val startDate: String,
    val endDate: String,
    val position: String,
    val activities: String,
    val imageUrl: String,
)