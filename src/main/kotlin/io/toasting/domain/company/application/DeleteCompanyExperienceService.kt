package io.toasting.domain.company.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler.CompanyExperienceNotFoundException
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler.UnauthorizedDeleteCompanyExperienceException
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler.CustomCompanyExperienceNotFoundException
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler.UnauthorizedDeleteCustomCompanyExperienceException
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteCompanyExperienceService(
    private val companyExperienceRepository: CompanyExperienceRepository,
    private val customCompanyExperienceRepository: CustomCompanyExperienceRepository
) {

    @Transactional(readOnly = false)
    fun deleteExistCompanyExperience(memberId: Long, experienceId: Long) {
        val experience = companyExperienceRepository.findById(experienceId)
            .orElseThrow { CompanyExperienceNotFoundException(ErrorStatus.COMPANY_EXPERIENCE_NOT_FOUND) }

        if (experience.memberId != memberId) {
            throw UnauthorizedDeleteCompanyExperienceException(ErrorStatus.UNAUTHORIZED_DELETE_COMPANY_EXPERIENCE)
        }

        companyExperienceRepository.delete(experience)
    }

    @Transactional(readOnly = false)
    fun deleteCustomCompanyExperience(memberId: Long, experienceId: Long) {
        val customExperience = customCompanyExperienceRepository.findById(experienceId)
            .orElseThrow { CustomCompanyExperienceNotFoundException(ErrorStatus.CUSTOM_COMPANY_EXPERIENCE_NOT_FOUND) }

        if (customExperience.memberId != memberId) {
            throw UnauthorizedDeleteCustomCompanyExperienceException(ErrorStatus.UNAUTHORIZED_DELETE_CUSTOM_COMPANY_EXPERIENCE)
        }

        customCompanyExperienceRepository.delete(customExperience)
    }
}