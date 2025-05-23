package io.toasting.domain.company.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import io.toasting.domain.company.exception.CompanyExceptionHandler.CompanyNotFoundException
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberNotFoundException
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddCompanyExperienceService(
    private val companyRepository: CompanyRepository,
    private val companyExperienceRepository: CompanyExperienceRepository,
    private val customCompanyExperienceRepository: CustomCompanyExperienceRepository,
    private val memberRepository: MemberRepository
) {
    @Transactional(readOnly = false)
    fun addExistCompanyExperience(input: AddExistCompanyExperienceInput) {
        memberRepository.findById(input.memberId)
            .orElseThrow { MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
        companyRepository.findById(input.companyId)
            .orElseThrow { CompanyNotFoundException(ErrorStatus.COMPANY_NOT_FOUND) }

        val companyExperience = input.toEntity()
        companyExperienceRepository.save(companyExperience)
    }

    @Transactional(readOnly = false)
    fun addCustomCompanyExperience(input: AddCustomCompanyExperienceInput) {
        memberRepository.findById(input.memberId)
            .orElseThrow { MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        val customCompanyExperience = input.toEntity()
        customCompanyExperienceRepository.save(customCompanyExperience)
    }
}