package io.toasting.domain.company.application

import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput
import org.springframework.stereotype.Service

@Service
class AddCompanyExperienceService {

    /*
     * 이미 존재하는 회사로 경력을 추가하는 경우
     */
    fun addExistCompanyExperience(input: AddExistCompanyExperienceInput) {
        //TODO
    }

    /*
     * 직접 입력으로 경력을 추가하는 경우
     */
    fun addCustomCompanyExperience(input: AddCustomCompanyExperienceInput) {
        //TODO
    }
}