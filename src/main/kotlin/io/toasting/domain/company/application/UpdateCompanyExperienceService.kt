package io.toasting.domain.company.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.application.input.*
import io.toasting.domain.company.exception.CompanyExceptionHandler.CompanyNotFoundException
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler.CompanyExperienceNotFoundException
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler.CustomCompanyExperienceNotFoundException
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateCompanyExperienceService(
    private val companyRepository: CompanyRepository,
    private val companyExperienceRepository: CompanyExperienceRepository,
    private val customCompanyExperienceRepository: CustomCompanyExperienceRepository,
) {

    @Transactional
    fun updateExistCompanyExperience(input: UpdateExistCompanyExperienceInput) {
        val company = companyRepository.findById(input.companyId)
            .orElseThrow { CompanyNotFoundException(ErrorStatus.COMPANY_NOT_FOUND) }
        val companyExperience = companyExperienceRepository.findById(input.experienceId)
            .orElseThrow { CompanyExperienceNotFoundException(ErrorStatus.COMPANY_EXPERIENCE_NOT_FOUND) }
        if (companyExperience.memberId != input.memberId) {
            throw CompanyExperienceExceptionHandler.UnauthorizedUpdateException(ErrorStatus.UNAUTHORIZED_UPDATE_COMPANY_EXPERIENCE)
        }

        val updatedCompanyExperience = input.toEntity(company, companyExperience)
        companyExperienceRepository.save(updatedCompanyExperience)
    }

    @Transactional
    fun updateCustomCompanyExperience(input: UpdateCustomCompanyExperienceInput) {
        val customCompanyExperience = customCompanyExperienceRepository.findById(input.experienceId)
            .orElseThrow { CustomCompanyExperienceNotFoundException(ErrorStatus.CUSTOM_COMPANY_EXPERIENCE_NOT_FOUND) }
        if (customCompanyExperience.memberId != input.memberId) {
            throw CustomCompanyExperienceExceptionHandler.UnauthorizedUpdateException(ErrorStatus.UNAUTHORIZED_UPDATE_CUSTOM_COMPANY_EXPERIENCE)
        }

        val updatedCustomCompanyExperience = input.toEntity(customCompanyExperience)
        customCompanyExperienceRepository.save(updatedCustomCompanyExperience)
    }

    @Transactional
    fun updateCustomCompanyExperienceIsView(input: UpdateCustomCompanyExperienceIsViewInput) {
        val customCompanyExperience = customCompanyExperienceRepository.findById(input.experienceId)
            .orElseThrow { CustomCompanyExperienceNotFoundException(ErrorStatus.CUSTOM_COMPANY_EXPERIENCE_NOT_FOUND) }
        if (customCompanyExperience.memberId != input.memberId) {
            throw CustomCompanyExperienceExceptionHandler.UnauthorizedUpdateIsViewException(ErrorStatus.UNAUTHORIZED_UPDATE_CUSTOM_COMPANY_EXPERIENCE_IS_VIEW)
        }

        val updatedCustomCompanyExperience = input.toEntity(customCompanyExperience)
        customCompanyExperienceRepository.save(updatedCustomCompanyExperience)
    }

    @Transactional
    fun updateExistCompanyExperienceIsView(input: UpdateExistCompanyExperienceIsViewInput) {
        val companyExperience = companyExperienceRepository.findById(input.experienceId)
            .orElseThrow { CompanyExperienceNotFoundException(ErrorStatus.COMPANY_EXPERIENCE_NOT_FOUND) }
        if (companyExperience.memberId != input.memberId) {
            throw CompanyExperienceExceptionHandler.UnauthorizedUpdateIsViewException(ErrorStatus.UNAUTHORIZED_UPDATE_COMPANY_EXPERIENCE_IS_VIEW)
        }

        val updatedCompanyExperience = input.toEntity(companyExperience)
        companyExperienceRepository.save(updatedCompanyExperience)
    }
}