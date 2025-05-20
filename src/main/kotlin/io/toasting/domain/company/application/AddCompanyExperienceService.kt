package io.toasting.domain.company.application

import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput
import io.toasting.domain.company.entity.CustomCompanyExperience
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import io.toasting.global.extension.toLocalDate
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AddCompanyExperienceService(
    private val companyExperienceRepository: CompanyExperienceRepository,
    private val companyRepository: CompanyRepository,
    private val customCompanyExperienceRepository: CustomCompanyExperienceRepository,
) {

    /*
     * 이미 존재하는 회사로 경력을 추가하는 경우
     */
    @Transactional
    fun addExistCompanyExperience(input: AddExistCompanyExperienceInput) {
        // TODO : 추후 작업
    }

    /*
     * 직접 입력으로 경력을 추가하는 경우
     */
    @Transactional
    fun addCustomCompanyExperience(input: AddCustomCompanyExperienceInput) {
        val entity = input.toCustomCompanyExperienceEntity()
        customCompanyExperienceRepository.save(entity)
    }

    private fun AddCustomCompanyExperienceInput.toCustomCompanyExperienceEntity(): CustomCompanyExperience {
        return CustomCompanyExperience.defaultEntity(
            // TODO : startDate와 endDate가 여기서 변환되는 것은 옳지 않음
            // TODO : Controller 단으로 옳겨서 예외처리 되는 것으로 develop PR 머지되면 controller로 구조 옮기기
            startDate = startDate.toLocalDate() ?: throw IllegalArgumentException("startDate is null"),
            endDate = endDate.toLocalDate() ?: throw IllegalArgumentException("endDate is null"),
            position = position,
            activities = activities,
            profileImage = imageUrl,
            companyName = name,
            memberId = memberId,
        )
    }
}