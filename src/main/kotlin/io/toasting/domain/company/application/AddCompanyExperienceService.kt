package io.toasting.domain.company.application

import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput
import io.toasting.domain.company.entity.CustomCompanyExperience
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AddCompanyExperienceService(
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
            startDate = startDate,
            endDate = endDate,
            position = position,
            activities = activities,
            profileImage = imageUrl,
            companyName = name,
            memberId = memberId,
        )
    }
}