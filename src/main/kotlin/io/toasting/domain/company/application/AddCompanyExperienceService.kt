package io.toasting.domain.company.application

import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddCompanyExperienceService(
    private val companyExperienceRepository: CompanyExperienceRepository,
    private val customCompanyExperienceRepository: CustomCompanyExperienceRepository,
) {
    @Transactional(readOnly = false)
    fun addExistCompanyExperience(input: AddExistCompanyExperienceInput) {
        val companyExperience = input.toEntity()
        companyExperienceRepository.save(companyExperience)
    }

    @Transactional(readOnly = false)
    fun addCustomCompanyExperience(input: AddCustomCompanyExperienceInput) {

        val customCompanyExperience = input.toEntity()
        customCompanyExperienceRepository.save(customCompanyExperience)
    }
}