package io.toasting.domain.company.application.input

import java.time.LocalDate

data class AddCustomCompanyExperienceInput(
    val memberId: Long,
    val companyId: Long,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val position: String,
    val activities: String,
    val imageUrl: String,
)