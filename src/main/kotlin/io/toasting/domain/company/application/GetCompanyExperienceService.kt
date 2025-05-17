package io.toasting.domain.company.application

import io.toasting.domain.company.application.output.GetCompanyExperienceOutput
import org.springframework.stereotype.Service

@Service
class GetCompanyExperienceService {
    fun getCompanyExperience(memberId: Long): GetCompanyExperienceOutput {
        // TODO
        return GetCompanyExperienceOutput("TODO")
    }
}