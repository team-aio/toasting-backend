package io.toasting.domain.company.application.input

data class UpdateExistCompanyExperienceIsViewInput(
    val memberId: Long,
    val experienceId: Long,
    val isView: Boolean,
    val isCustom: Boolean
)
