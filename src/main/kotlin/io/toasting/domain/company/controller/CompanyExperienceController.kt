package io.toasting.domain.company.controller

import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.controller.request.AddCompanyExperienceRequest
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
class CompanyExperienceController {

    @PostMapping("{memberId}/experience/company")
    fun addCompanyExperience(
        @PathVariable memberId: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestBody request: AddCompanyExperienceRequest,
    ) {
        val memberUuid = memberDetails.username
        if (memberId != memberUuid) {
            throw MemberException(ErrorStatus.MEMBER_NOT_MINE)
        }
    }
}