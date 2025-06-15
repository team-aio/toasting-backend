package io.toasting.domain.company.application

import io.toasting.domain.company.application.output.GetCompanyExperienceOutput
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import org.springframework.stereotype.Service

@Service
class GetCompanyExperienceService(
    private val companyRepository: CompanyRepository,
    private val companyExperienceRepository: CompanyExperienceRepository,
    private val customCompanyExperienceRepository: CustomCompanyExperienceRepository,
) {
    fun getCompanyExperience(memberId: Long): List<GetCompanyExperienceOutput> {
        val companyExperienceList = companyExperienceRepository.findByMemberId(memberId)
        val companyIdList = companyExperienceList.map { it.companyId }
        val companyMap = companyRepository.findAllById(companyIdList)
            .associateBy { it.id }

        val customCompanyExperienceList = customCompanyExperienceRepository.findByMemberId(memberId)

        val outputs = mutableListOf<GetCompanyExperienceOutput>()
        outputs += companyExperienceList.mapNotNull { experience ->
            val company = companyMap[experience.companyId] ?: return@mapNotNull null
            GetCompanyExperienceOutput.of(experience, company)
        }

        outputs += customCompanyExperienceList.map { customExperience ->
            GetCompanyExperienceOutput.of(customExperience)
        }

        return outputs.sortedWith(
            compareBy<GetCompanyExperienceOutput>(
                { it.endDate == null } // endDate가 null인 것이 먼저
            ).thenByDescending { it.endDate } // endDate 내림차순(null 아닌 경우)
                .thenByDescending { it.startDate } // startDate 내림차순

        )
    }
}