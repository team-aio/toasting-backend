package io.toasting.domain.company.application

import io.toasting.domain.company.application.input.UpdateCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.UpdateExistCompanyExperienceInput
import org.springframework.stereotype.Service

@Service
class UpdateCompanyExperienceService {

    /*
     * 이미 존재하는 회사로 경력을 수정하는 경우
     */
    fun updateExistCompanyExperience(input: UpdateExistCompanyExperienceInput) {
        //TODO
    }

    /*
     * 직접 입력으로 경력을 수정하는 경우
     */
    fun updateCustomCompanyExperience(input: UpdateCustomCompanyExperienceInput) {
        //TODO
    }
}