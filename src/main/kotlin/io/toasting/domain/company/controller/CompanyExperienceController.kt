package io.toasting.domain.company.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.application.AddCompanyExperienceService
import io.toasting.domain.company.controller.request.AddCompanyExperienceRequest
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberException
import io.toasting.global.api.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
class CompanyExperienceController(
    private val addCompanyExperienceService: AddCompanyExperienceService
) {

    // TODO : 로그인을 안해도 해당 API를 호출할 수 있도록 JWT Filter를 뚫어놔야 함(현재 안뚫려있음)
    @GetMapping("{memberId}/experience/company")
    @Operation(summary = "유저 회사 경력 조회", description = "회사 경력을 조회합니다. 로그인이 필요 없습니다.")
    fun getCompanyExperience(
        @PathVariable memberId: String,
    ): ApiResponse<Unit> {
        return ApiResponse.onSuccess()
    }

    @PostMapping("{memberId}/experience/company")
    @Operation(summary = "유저 회사 경력 추가", description = "회사 경력을 추가합니다.")
    fun addCompanyExperience(
        @PathVariable memberId: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestBody request: AddCompanyExperienceRequest,
    ): ApiResponse<Unit> {
        val memberUuid = memberDetails.username
        if (memberId != memberUuid) {
            throw MemberException(ErrorStatus.MEMBER_NOT_MINE)
        }

        when (request.isCustom) {
            true -> addCompanyExperienceService.addCustomCompanyExperience(request.toCustomInput())
            false -> addCompanyExperienceService.addExistCompanyExperience(request.toExistInput())
        }
        return ApiResponse.onSuccess()
    }
}