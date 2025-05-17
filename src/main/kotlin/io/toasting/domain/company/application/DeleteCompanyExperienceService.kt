package io.toasting.domain.company.application

import org.springframework.stereotype.Service

@Service
class DeleteCompanyExperienceService {

    /*
     * 이미 존재하는 회사로 경력을 삭제하는 경우
     */
    fun deleteExistCompanyExperience(memberId: Long, experienceId: Long) {
        //TODO
    }

    /*
     * 직접 입력으로 경력을 삭제하는 경우
     */
    fun deleteCustomCompanyExperience(memberId: Long, experienceId: Long) {
        //TODO
    }
}